package ch.wisv.chue.events;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;
import ch.wisv.chue.hue.NotExecutedException;

/**
 * Alert event
 */
public class Alert implements HueEvent {

    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        if (lightIdentifiers.length == 0) {
            throw new EventNotExecutedException("No lights affected (is the bridge offline?)");
        }

        for (String id : lightIdentifiers) {
            HueLightState lightState = new HueLightState();
            lightState.setTransitionTime(0);
            lightState.setAlertMode(HueLightState.AlertMode.LSELECT);

            try {
                hueFacade.updateLightState(id, lightState);
            } catch (NotExecutedException e) {
                throw new EventNotExecutedException(e.getMessage());
            }
        }
    }
}
