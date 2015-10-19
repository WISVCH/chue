package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLightState;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Random color loop state
 */
public class RandomColorState implements HueState {

    private Map<String, Color> lightColors = new HashMap<>();

    public Map<String, Color> getLightColors() {
        return lightColors;
    }

    @Override
    public void execute(PHBridge bridge, String... lightIdentifiers) {
        Random rand = new Random();

        for (String id : lightIdentifiers) {
            int randHue = rand.nextInt(HueFacade.MAX_HUE);

            PHLightState lightState = new PHLightState();
            lightState.setHue(randHue);
            lightState.setSaturation(HueFacade.MAX_SATURATION);
            lightState.setBrightness(HueFacade.MAX_BRIGHTNESS);

            lightColors.put(id, Color.hsb((double) (randHue * 360) / HueFacade.MAX_HUE, 1, 1));

            bridge.updateLightState(id, lightState, null); // If no bridge response is required then use this simpler form.
        }
    }
}
