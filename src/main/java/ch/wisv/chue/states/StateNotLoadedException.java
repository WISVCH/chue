package ch.wisv.chue.states;

import ch.wisv.chue.HueCommandNotExecutedException;

public class StateNotLoadedException extends HueCommandNotExecutedException {
    public StateNotLoadedException() {
        super();
    }

    public StateNotLoadedException(Throwable cause) {
        super(cause);
    }
}
