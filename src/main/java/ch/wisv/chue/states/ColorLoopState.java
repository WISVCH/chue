package ch.wisv.chue.states;

import ch.wisv.chue.hue.BridgeUnavailableException;
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

            try {
                hueFacade.updateLightState(lightId, lightState);
            } catch (BridgeUnavailableException e) {
                throw new StateNotLoadedException(e);
            }
        }
    }
}
