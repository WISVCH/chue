package ch.wisv.chue.hue;

import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.OptionalInt;

public class HueLightState {
    private Optional<AlertMode> alertMode = Optional.empty();
    private Optional<EffectMode> effectMode = Optional.empty();
    private OptionalInt transitionTime = OptionalInt.empty();
    private Optional<Color> color = Optional.empty();

    public Optional<AlertMode> getAlertMode() {
        return alertMode;
    }

    public void setAlertMode(AlertMode alertMode) {
        this.alertMode = Optional.of(alertMode);
    }

    public Optional<EffectMode> getEffectMode() {
        return effectMode;
    }

    public void setEffectMode(EffectMode effectMode) {
        this.effectMode = Optional.of(effectMode);
    }

    public OptionalInt getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(int transitionTime) {
        this.transitionTime = OptionalInt.of(transitionTime);
    }

    public Optional<Color> getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = Optional.of(color);
    }

    public enum AlertMode {
        NONE, LSELECT
    }

    public enum EffectMode {
        NONE, COLORLOOP
    }

    @Override
    public String toString() {
        return "HueLightState{" +
                "color=" + color +
                ", alertMode=" + alertMode +
                ", effectMode=" + effectMode +
                ", transitionTime=" + transitionTime +
                '}';
    }
}
