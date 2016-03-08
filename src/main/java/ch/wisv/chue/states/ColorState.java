package ch.wisv.chue.states;

import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.hue.HueLightState;
import javafx.scene.paint.Color;

import java.util.Set;

/**
 * Color state
 */
public class ColorState implements HueState {

    private Color color;

    public ColorState(Color color) {
        this.color = color;
    }

    @Override
    public void execute(HueFacade hueFacade, Set<HueLamp> lamps) {
        for (HueLamp lamp : lamps) {
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
