package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;

/**
 * Color loop state
 */
public class ColorLoopState implements HueState {
    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        for (String lightId : lightIdentifiers) {
            HueLightState lightState = new HueLightState();
            lightState.setEffectMode(HueLightState.EffectMode.COLORLOOP);
            hueFacade.updateLightState(lightId, lightState);
        }
    }
}
