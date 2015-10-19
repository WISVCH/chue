package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;
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
            HueLightState lightState = new HueLightState();
            lightState.setColor(color);

            lightColors.put(id, color);

            hueFacade.updateLightState(id, lightState); // If no bridge response is required then use this simpler form.
        }
    }
}
