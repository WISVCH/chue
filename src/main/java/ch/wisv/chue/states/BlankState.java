package ch.wisv.chue.states;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.hue.HueLightState;

import java.util.Set;

/**
 * Blank state
 */
public class BlankState implements HueState {

    @Override
    public void execute(HueFacade hueFacade, Set<HueLamp> lamps) {
        for (HueLamp lamp : lamps) {
            HueLightState lightState = new HueLightState();
            lightState.setEffectMode(HueLightState.EffectMode.NONE);
            lightState.setAlertMode(HueLightState.AlertMode.NONE);
            lightState.setTransitionTime(400);

            try {
                hueFacade.updateLightState(lamp, lightState);
            } catch (BridgeUnavailableException e) {
                throw new StateNotLoadedException(e);
            }
        }
    }
}
