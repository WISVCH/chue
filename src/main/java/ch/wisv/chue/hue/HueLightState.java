package ch.wisv.chue.hue;

import javafx.scene.paint.Color;

import java.util.Optional;

public class HueLightState {
    private AlertMode alertMode;
    private EffectMode effectMode;
    private Integer transitionTime;
    private Color color;

    public Optional<AlertMode> getAlertMode() {
        return Optional.ofNullable(alertMode);
    }

    public void setAlertMode(AlertMode alertMode) {
        this.alertMode = alertMode;
    }

    public Optional<EffectMode> getEffectMode() {
        return Optional.ofNullable(effectMode);
    }

    public void setEffectMode(EffectMode effectMode) {
        this.effectMode = effectMode;
    }

    public Optional<Integer> getTransitionTime() {
        return Optional.ofNullable(transitionTime);
    }

    public void setTransitionTime(int transitionTime) {
        this.transitionTime = transitionTime;
    }

    public Optional<Color> getColor() {
        return Optional.ofNullable(color);
    }

    public void setColor(Color color) {
        this.color = color;
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
