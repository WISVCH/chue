package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

/**
 * Color loop state
 */
public class ColorLoopState implements HueState {
    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        for (String lightId : lightIdentifiers) {
            PHLightState lightState = new PHLightState();
            lightState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_COLORLOOP);
            hueFacade.updateLightState(lightId, lightState); // If no bridge response is required then use this simpler form.
        }
    }
}
