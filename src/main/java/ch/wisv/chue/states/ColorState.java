package ch.wisv.chue.states;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;
import ch.wisv.chue.hue.NotExecutedException;
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
        if (lightIdentifiers.length == 0) {
            throw new StateNotLoadedException("No lights affected (is the bridge offline?)");
        }

        for (String id : lightIdentifiers) {
            HueLightState lightState = new HueLightState();
            lightState.setColor(color);

            try {
                hueFacade.updateLightState(id, lightState);
            } catch (NotExecutedException e) {
                throw new StateNotLoadedException(e.getMessage());
            }

            lightColors.put(id, color);
        }
    }
}
