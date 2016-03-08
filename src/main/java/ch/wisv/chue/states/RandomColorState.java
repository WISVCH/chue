package ch.wisv.chue.states;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.hue.HueLightState;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.Set;

/**
 * Random color loop state
 */
public class RandomColorState implements HueState {

    @Override
    public void execute(HueFacade hueFacade, Set<HueLamp> lamps) {
        Random rand = new Random();

        for (HueLamp lamp : lamps) {
            Color color = Color.hsb(rand.nextDouble() * 360, 1, 1);

            HueLightState lightState = new HueLightState();
            lightState.setColor(color);

            try {
                hueFacade.updateLightState(lamp, lightState);
            } catch (BridgeUnavailableException e) {
                throw new StateNotLoadedException(e);
            }
        }
    }
}
