package ch.wisv.chue.events;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.hue.HueLightState;

import java.util.Set;

/**
 * Alert event
 */
public class Alert implements HueEvent {

    @Override
    public void execute(HueFacade hueFacade, Set<HueLamp> lamps) {
        for (HueLamp lamp : lamps) {
            HueLightState lightState = new HueLightState();
            lightState.setTransitionTime(0);
            lightState.setAlertMode(HueLightState.AlertMode.LSELECT);

            try {
                hueFacade.updateLightState(lamp, lightState);
            } catch (BridgeUnavailableException e) {
                throw new EventNotExecutedException(e);
            }
        }
    }
}
