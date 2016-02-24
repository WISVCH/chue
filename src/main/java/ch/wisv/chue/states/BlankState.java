package ch.wisv.chue.states;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;

/**
 * Blank state
 */
public class BlankState implements HueState {

    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        for (String id : lightIdentifiers) {
            HueLightState lightState = new HueLightState();
            lightState.setEffectMode(HueLightState.EffectMode.NONE);
            lightState.setAlertMode(HueLightState.AlertMode.NONE);
            lightState.setTransitionTime(400);

            try {
                hueFacade.updateLightState(id, lightState);
            } catch (BridgeUnavailableException e) {
                throw new StateNotLoadedException(e.getMessage());
            }
        }
    }
}
