package ch.wisv.chue.events;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;

/**
 * Alert event
 */
public class Alert implements HueEvent {

    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        for (String id : lightIdentifiers) {
            HueLightState lightState = new HueLightState();
            lightState.setTransitionTime(0);
            lightState.setAlertMode(HueLightState.AlertMode.LSELECT);

            try {
                hueFacade.updateLightState(id, lightState);
            } catch (BridgeUnavailableException e) {
                throw new EventNotExecutedException(e);
            }
        }
    }
}
