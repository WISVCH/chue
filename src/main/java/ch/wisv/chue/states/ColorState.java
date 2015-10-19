package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLightState;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Color state
 */
public class ColorState implements HueState {

    private Color color;

    private Map<String, Color> lightColors = new HashMap<>();

    public ColorState(Color color) {
        this.color = color;
    }

    public Map<String, Color> getLightColors() {
        return lightColors;
    }

    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        for (String id : lightIdentifiers) {
            PHLightState lightState = new PHLightState();
            float xy[] = PHUtilities.calculateXYFromRGB(
                    +(int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255), "LCT001");
            lightState.setX(xy[0]);
            lightState.setY(xy[1]);

            lightColors.put(id, color);

            hueFacade.updateLightState(id, lightState); // If no bridge response is required then use this simpler form.
        }
    }
}
