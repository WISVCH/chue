package ch.wisv.chue.states;

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
            hueFacade.updateLightState(id, lightState);
        }
    }
}
