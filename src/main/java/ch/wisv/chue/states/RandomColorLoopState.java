package ch.wisv.chue.states;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLightState;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Random color loop state
 */
public class RandomColorLoopState implements HueState {
    @Override
    public void execute(HueFacade hueFacade, String... lightIdentifiers) {
        Random rand = new Random();

        for (String lightId : lightIdentifiers) {
            Color color = Color.hsb(rand.nextDouble() * 360, 1, 1);

            HueLightState lightState = new HueLightState();
            lightState.setEffectMode(HueLightState.EffectMode.COLORLOOP);
            lightState.setColor(color);

            try {
                hueFacade.updateLightState(lightId, lightState);
            } catch (BridgeUnavailableException e) {
                throw new StateNotLoadedException(e);
            }
        }
    }
}
