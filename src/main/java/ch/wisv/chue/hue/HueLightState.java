package ch.wisv.chue.hue;

import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.OptionalInt;

public class HueLightState {
    private Optional<AlertMode> alertMode = Optional.empty();
    private Optional<EffectMode> effectMode = Optional.empty();
    private OptionalInt transitionTime = OptionalInt.empty();
    private OptionalInt hue = OptionalInt.empty();
    private OptionalInt brightness = OptionalInt.empty();
    private OptionalInt saturation = OptionalInt.empty();
    private Optional<Color> color = Optional.empty();

    protected static PHLightState asPHLightState(HueLightState hueLightState) {
        PHLightState phLightState = new PHLightState();

        if (hueLightState.getHue().isPresent()) {
            phLightState.setHue(hueLightState.getHue().getAsInt());
        }

        if (hueLightState.getSaturation().isPresent()) {
            phLightState.setSaturation(hueLightState.getSaturation().getAsInt());
        }

        if (hueLightState.getBrightness().isPresent()) {
            phLightState.setSaturation(hueLightState.getBrightness().getAsInt());
        }

        if (hueLightState.getTransitionTime().isPresent()) {
            phLightState.setTransitionTime(hueLightState.getTransitionTime().getAsInt());
        }

        if (hueLightState.getAlertMode().isPresent()) {
            switch (hueLightState.getAlertMode().get()) {
                case NONE:
                    phLightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_NONE);
                    break;
                case LSELECT:
                    phLightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_LSELECT);
                    break;
            }
        }

        if (hueLightState.getEffectMode().isPresent()) {
            switch (hueLightState.getEffectMode().get()) {
                case NONE:
                    phLightState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_NONE);
                    break;
                case COLORLOOP:
                    phLightState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_COLORLOOP);
                    break;
            }
        }

        if (hueLightState.getColor().isPresent()) {
            float xy[] = PHUtilities.calculateXYFromRGB(
                    (int) (hueLightState.getColor().get().getRed() * 255),
                    (int) (hueLightState.getColor().get().getGreen() * 255),
                    (int) (hueLightState.getColor().get().getBlue() * 255),
                    "LCT001");
            phLightState.setX(xy[0]);
            phLightState.setY(xy[1]);
        }

        return phLightState;
    }

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

    public OptionalInt getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = OptionalInt.of(hue);
    }

    public OptionalInt getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = OptionalInt.of(brightness);
    }

    public OptionalInt getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = OptionalInt.of(saturation);
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
                "alertMode=" + alertMode +
                ", effectMode=" + effectMode +
                ", transitionTime=" + transitionTime +
                ", hue=" + hue +
                ", brightness=" + brightness +
                ", saturation=" + saturation +
                ", color=" + color +
                '}';
    }
}
