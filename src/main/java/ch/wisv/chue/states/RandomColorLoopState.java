package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;

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

            HueLightState lightState = new HueLightState();
            lightState.setEffectMode(HueLightState.EffectMode.COLORLOOP);
            lightState.setHue(randHue);
            hueFacade.updateLightState(lightId, lightState); // If no bridge response is required then use this simpler form.
        }
    }
}
