package ch.wisv.chue;

public class HueCommandNotExecutedException extends RuntimeException {
    public HueCommandNotExecutedException() {
        super("Hue bridge is not available");
    }

    public HueCommandNotExecutedException(Throwable cause) {
        super(cause);
    }
}
