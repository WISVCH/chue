package ch.wisv.chue.hue;

public class BridgeUnavailableException extends Exception {
    public BridgeUnavailableException() {
        super("Hue bridge is not available");
    }
}
