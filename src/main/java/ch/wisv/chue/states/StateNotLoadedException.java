package ch.wisv.chue.states;

import ch.wisv.chue.HueCommandNotExecutedException;

public class StateNotLoadedException extends HueCommandNotExecutedException {
    public StateNotLoadedException(String message) {
        super(message);
    }

    public StateNotLoadedException(Throwable cause) {
        super(cause);
    }
}
