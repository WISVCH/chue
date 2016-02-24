package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;
import ch.wisv.chue.hue.NotExecutedException;

/**
 * Color loop state
 */
public class ColorLoopState implements HueState {
    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        if (lightIdentifiers.length == 0) {
            throw new StateNotLoadedException("No lights affected (is the bridge offline?)");
        }

        for (String lightId : lightIdentifiers) {
            HueLightState lightState = new HueLightState();
            lightState.setEffectMode(HueLightState.EffectMode.COLORLOOP);

            try {
                hueFacade.updateLightState(lightId, lightState);
            } catch (NotExecutedException e) {
                throw new StateNotLoadedException(e.getMessage());
            }
        }
    }
}
