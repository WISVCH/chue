package ch.wisv.chue.states;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;
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
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        Random rand = new Random();

        for (String id : lightIdentifiers) {
            Color color = Color.hsb(rand.nextDouble() * 360, 1, 1);

            HueLightState lightState = new HueLightState();
            lightState.setColor(color);

            try {
                hueFacade.updateLightState(id, lightState);
            } catch (BridgeUnavailableException e) {
                throw new StateNotLoadedException(e.getMessage());
            }

            lightColors.put(id, color);
        }
    }
}
