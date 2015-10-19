package ch.wisv.chue.events;

import ch.wisv.chue.hue.HueFacade;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

/**
 * Alert event
 */
public class Alert implements HueEvent {

    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        for (String id : lightIdentifiers) {
            PHLightState lightState = new PHLightState();
            lightState.setTransitionTime(0);
            lightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_LSELECT);
            hueFacade.updateLightState(id, lightState);
        }
    }
}
