package ch.wisv.chue.states;

import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

/**
 * Blank state
 */
public class BlankState implements HueState {

    @Override
    public void execute(PHBridge bridge, String... lightIdentifiers) {

        for (String id : lightIdentifiers) {
            PHLightState lightState = new PHLightState();
            lightState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_NONE);
            lightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_NONE);
            lightState.setTransitionTime(400);
            bridge.updateLightState(id, lightState, null); // If no bridge response is required then use this simpler form.
        }
    }
}
