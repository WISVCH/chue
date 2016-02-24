package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;
import ch.wisv.chue.hue.NotExecutedException;

/**
 * Blank state
 */
public class BlankState implements HueState {

    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        if (lightIdentifiers.length == 0) {
            throw new StateNotLoadedException("No lights affected (is the bridge offline?)");
        }

        for (String id : lightIdentifiers) {
            HueLightState lightState = new HueLightState();
            lightState.setEffectMode(HueLightState.EffectMode.NONE);
            lightState.setAlertMode(HueLightState.AlertMode.NONE);
            lightState.setTransitionTime(400);

            try {
                hueFacade.updateLightState(id, lightState);
            } catch (NotExecutedException e) {
                throw new StateNotLoadedException(e.getMessage());
            }
        }
    }
}
