package ch.wisv.chue;

public class HueCommandNotExecutedException extends RuntimeException {
    public HueCommandNotExecutedException(String message) {
        super(message);
    }

    public HueCommandNotExecutedException(Throwable cause) {
        super(cause);
    }
}
