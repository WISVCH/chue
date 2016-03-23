package ch.wisv.chue.hue;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.hue.sdk.bridge.impl.PHBridgeImpl;
import com.philips.lighting.hue.sdk.connection.impl.PHLocalBridgeDelegator;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class PhilipsHueFacade implements HueFacade {

    private static final Logger log = LoggerFactory.getLogger(PhilipsHueFacade.class);

    @Value("${BridgeUsername}")
    private String username;
    @Value("${BridgeHostname}")
    private String hostname;

    private PHHueSDK phHueSDK = PHHueSDK.getInstance();

    private PHBridge bridge;

    private SortedMap<String, HueLamp> lamps = new TreeMap<>();

    /**
     * Connect to the last known access point.
     * This method is triggered by the Connect to Bridge button but it can equally be used to automatically connect
     * * to a bridge.
     */
    @PostConstruct
    public void connectToBridge() {
        log.info("Connecting to bridge");
        if (username == null || hostname == null) {
            throw new RuntimeException("Missing hostname or username.");
        }
        phHueSDK.getNotificationManager().registerSDKListener(listener);

        // To enable pushlink authentication:
        // PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        // sm.search(true, true);

        PHAccessPoint accessPoint = new PHAccessPoint();
        accessPoint.setUsername(username);
        accessPoint.setIpAddress(hostname);
        phHueSDK.connect(accessPoint);
    }

    private PHSDKListener listener = new PHSDKListener() {

        private void updateLampMap() {
            SortedMap<String, HueLamp> newLamps = new TreeMap<>();

            for (PHLight lamp : bridge.getResourceCache().getAllLights()) {
                if (lamps.containsKey(lamp.getIdentifier())) {
                    newLamps.put(lamp.getIdentifier(), lamps.get(lamp.getIdentifier()));
                } else {
                    newLamps.put(lamp.getIdentifier(), new HueLamp(lamp.getIdentifier(), lamp.getName()));
                }
            }

            lamps = newLamps;
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPointsList) {
            PHAccessPoint accessPoint = accessPointsList.get(0);
            log.warn("Found {} bridges, connecting to first bridge: {}", accessPointsList.size(), accessPoint
                    .getIpAddress());
            phHueSDK.connect(accessPoint);
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint accessPoint) {
            log.warn("Authentication required, push button");
            phHueSDK.startPushlinkAuthentication(accessPoint);
        }

        @Override
        public void onBridgeConnected(PHBridge bridge, String username) {
            phHueSDK.setSelectedBridge(bridge);
            phHueSDK.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);
            PhilipsHueFacade.this.bridge = bridge;
            updateLampMap();
            log.info("Connected to bridge, username {}", username);
        }

        @Override
        public void onCacheUpdated(List<Integer> arg0, PHBridge arg1) {
        }

        @Override
        public void onConnectionLost(PHAccessPoint arg0) {
            if (isBridgeAvailable()) {
                log.warn("Lost connection with bridge");
                bridge = null;
            }
        }

        @Override
        public void onConnectionResumed(PHBridge bridge) {
            if (!isBridgeAvailable()) {
                log.info("Restored connection with bridge");
                PhilipsHueFacade.this.bridge = bridge;
                updateLampMap();
            }
        }

        @Override
        public void onError(int code, final String message) {
            if (code == PHHueError.BRIDGE_NOT_RESPONDING) {
                log.error("Not responding");
            } else if (code == PHMessageType.PUSHLINK_BUTTON_NOT_PRESSED) {
                log.error("Pushlink button not pressed");
            } else if (code == PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
                log.error("Authentication failed");
            } else if (code == PHMessageType.BRIDGE_NOT_FOUND) {
                log.error("Not found");
            } else if (code != 22) { // magic number for 'No connection' message
                log.error("Error {}: {}", code, message);
            }
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
            for (PHHueParsingError parsingError : parsingErrorsList) {
                log.error("ParsingError: " + parsingError.getMessage());
            }
        }
    };

    private String buildBridgeHttpAddress() {
        return ((PHLocalBridgeDelegator) ((PHBridgeImpl) bridge).getBridgeDelegator())
                .buildHttpAddress().toString();
    }

    @Override
    public SortedMap<String, HueLamp> getAvailableLamps() {
        if (!isBridgeAvailable()) {
            return Collections.emptySortedMap();
        }

        return lamps;
    }

    @Override
    public void updateLightState(HueLamp lamp, HueLightState lightState) throws BridgeUnavailableException {
        if (!isBridgeAvailable()) {
            log.warn("Light state not updated: bridge not available!");
            throw new BridgeUnavailableException();
        }

        PHLightState phLightState = new PHLightState();

        if (lightState.getTransitionTime().isPresent()) {
            phLightState.setTransitionTime(lightState.getTransitionTime().get());
        }

        if (lightState.getAlertMode().isPresent()) {
            switch (lightState.getAlertMode().get()) {
                case NONE:
                    phLightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_NONE);
                    break;
                case LSELECT:
                    phLightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_LSELECT);
                    break;
            }
        }

        if (lightState.getEffectMode().isPresent()) {
            switch (lightState.getEffectMode().get()) {
                case NONE:
                    phLightState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_NONE);
                    break;
                case COLORLOOP:
                    phLightState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_COLORLOOP);
                    break;
            }
        }

        if (lightState.getColor().isPresent()) {
            float xy[] = PHUtilities.calculateXYFromRGB(
                    (int) Math.round(lightState.getColor().get().getRed() * 255.0),
                    (int) Math.round(lightState.getColor().get().getGreen() * 255.0),
                    (int) Math.round(lightState.getColor().get().getBlue() * 255.0),
                    "LCT001");
            phLightState.setX(xy[0]);
            phLightState.setY(xy[1]);
        }

        bridge.updateLightState(lamp.getId(), phLightState, null);
        lamp.setLastState(lightState);
    }

    @Override
    public boolean isBridgeAvailable() {
        return bridge != null;
    }

    /**
     * Check for Hue software update, and install it if available.
     * <p>
     * http://www.developers.meethue.com/documentation/software-update
     */
    @Scheduled(cron = "54 54 2 * * *")
    public void updateBridgeFirmware() {
        RestTemplate rt = new RestTemplate();
        String bridgeConfigUrl = buildBridgeHttpAddress() + "config";
        HueSWUpdate swUpdate = rt.getForEntity(bridgeConfigUrl, HueConfig.class).getBody().getSWUpdate();

        if (swUpdate.isNotify()) {
            log.info("Bridge update completed");
            rt.put(bridgeConfigUrl, "{\"swupdate\": {\"notify\":false}}");
        } else if (swUpdate.getUpdatestate() == 0) {
            log.info("Initiating bridge update check");
            rt.put(bridgeConfigUrl, "{\"swupdate\": {\"checkforupdate\":true}}");
        } else if (swUpdate.getUpdatestate() == 2) {
            log.info("Installing bridge update: {}", swUpdate.getText());
            rt.put(bridgeConfigUrl, "{\"swupdate\": {\"updatestate\":3}}");
        }
    }

    private static class HueConfig {
        private HueSWUpdate swupdate;

        public HueSWUpdate getSWUpdate() {
            return swupdate;
        }
    }

    private static class HueSWUpdate {
        private String text;
        private int updatestate;
        private boolean notify;

        public String getText() {
            return text;
        }

        public int getUpdatestate() {
            return updatestate;
        }

        public boolean isNotify() {
            return notify;
        }
    }
}
