package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.Random;

/**
 * Random color loop state
 */
public class RandomColorLoopState implements HueState {
    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        Random rand = new Random();

        for (String lightId : lightIdentifiers) {
            int randHue = rand.nextInt(HueFacade.MAX_HUE);

            PHLightState lightState = new PHLightState();
            lightState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_COLORLOOP);
            lightState.setHue(randHue);
            hueFacade.updateLightState(lightId, lightState); // If no bridge response is required then use this simpler form.
        }
    }
}
