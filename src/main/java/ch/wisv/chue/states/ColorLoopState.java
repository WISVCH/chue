package ch.wisv.chue.states;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.hue.HueLightState;

import java.util.List;

/**
 * Color loop state
 */
public class ColorLoopState implements HueState {

    @Override
    public void execute(HueFacade hueFacade, List<HueLamp> lamps) {
        for (HueLamp lamp : lamps) {
            HueLightState lightState = new HueLightState();
            lightState.setEffectMode(HueLightState.EffectMode.COLORLOOP);

            try {
                hueFacade.updateLightState(lamp, lightState);
            } catch (BridgeUnavailableException e) {
                throw new StateNotLoadedException(e);
            }
        }
    }
}
