package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

/**
 * Blank state
 */
public class BlankState implements HueState {

    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {

        for (String id : lightIdentifiers) {
            PHLightState lightState = new PHLightState();
            lightState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_NONE);
            lightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_NONE);
            lightState.setTransitionTime(400);
            hueFacade.updateLightState(id, lightState); // If no bridge response is required then use this simpler form.
        }
    }
}
