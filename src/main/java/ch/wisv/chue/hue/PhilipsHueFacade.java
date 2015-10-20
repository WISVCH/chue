package ch.wisv.chue.hue;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.hue.sdk.bridge.impl.PHBridgeImpl;
import com.philips.lighting.hue.sdk.connection.impl.PHHueHttpConnection;
import com.philips.lighting.hue.sdk.connection.impl.PHLocalBridgeDelegator;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.*;
import org.json.hue.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

public class PhilipsHueFacade implements HueFacade {

    private static final Logger log = LoggerFactory.getLogger(PhilipsHueFacade.class);

    @Value("${BridgeUsername}")
    private String username;
    @Value("${BridgeHostname}")
    private String hostname;

    private PHHueSDK phHueSDK = PHHueSDK.getInstance();

    private PHBridge bridge;

    /**
     * Connect to the last known access point.
     * This method is triggered by the Connect to Bridge button but it can equally be used to automatically connect
     * * to a bridge.
     */
    @PostConstruct
    public void connectToBridge() {
        log.info("Connecting");
        if (username == null || hostname == null) {
            throw new RuntimeException("Missing hostname or username.");
        }
        phHueSDK.getNotificationManager().registerSDKListener(listener);
        PHAccessPoint accessPoint = new PHAccessPoint();
        accessPoint.setUsername(username);
        accessPoint.setIpAddress(hostname);
        phHueSDK.connect(accessPoint);
    }

    private PHSDKListener listener = new PHSDKListener() {

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPointsList) {
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint accessPoint) {
        }

        @Override
        public void onBridgeConnected(PHBridge bridge) {
            phHueSDK.setSelectedBridge(bridge);
            phHueSDK.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);
            PhilipsHueFacade.this.bridge = bridge;
            log.info("Connected");
        }

        @Override
        public void onCacheUpdated(List<Integer> arg0, PHBridge arg1) {
        }

        @Override
        public void onConnectionLost(PHAccessPoint arg0) {
        }

        @Override
        public void onConnectionResumed(PHBridge arg0) {
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
            }
            log.error("\tMessage: " + message);
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
            for (PHHueParsingError parsingError : parsingErrorsList) {
                System.out.println("ParsingError : " + parsingError.getMessage());
            }
        }
    };

    @Override
    public void strobe(int millis, String... lightIdentifiers) {
        PHHueHttpConnection connection = new PHHueHttpConnection();
        final String httpAddress = ((PHLocalBridgeDelegator) ((PHBridgeImpl) bridge).getBridgeDelegator())
                .buildHttpAddress().toString();

        // Put a light definition aka `symbol` at bulb, using internal API call
        for (String light : lightIdentifiers) {
            JSONObject pointSymbol = new JSONObject();
            pointSymbol.put("1", "0A00F1F01F1F1001F1FF100000000001F2F");
            String resp = connection.putData(pointSymbol.toString(), httpAddress + "lights/" + light + "/pointsymbol");
            log.debug(resp);
        }

        boolean allLightsTurnedOn = bridge.getResourceCache().getAllLights().stream()
                .allMatch(light -> light.getLastKnownLightState().isOn());

        if (allLightsTurnedOn) {
            // Activate symbol
            // Kinda magic symbolselection. It is something like this:
            // for 01..05 step 01, [0i0x]+ where i is `symbol` and x is light bulb
            JSONObject strobeJSON = new JSONObject();
            strobeJSON.put("symbolselection", "01010301010102010301");
            strobeJSON.put("duration", millis);
            //group 0 contains all lights
            String resp = connection.putData(strobeJSON.toString(), httpAddress + "groups/0/transmitsymbol");
            log.debug(resp);
        }
    }

    @Override
    public List<HueLamp> getAllLamps() {
        return bridge.getResourceCache().getAllLights().stream()
                .map(l -> new HueLamp(l.getIdentifier(), l.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateLightState(String id, HueLightState lightState) {
        PHLightState phLightState = new PHLightState();

        if (lightState.getTransitionTime().isPresent()) {
            phLightState.setTransitionTime(lightState.getTransitionTime().getAsInt());
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
                    (int) (lightState.getColor().get().getRed() * 255),
                    (int) (lightState.getColor().get().getGreen() * 255),
                    (int) (lightState.getColor().get().getBlue() * 255),
                    "LCT001");
            phLightState.setX(xy[0]);
            phLightState.setY(xy[1]);
        }

        bridge.updateLightState(id, phLightState, null);
    }
}
