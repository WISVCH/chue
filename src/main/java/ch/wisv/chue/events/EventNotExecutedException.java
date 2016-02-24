package ch.wisv.chue.events;

import ch.wisv.chue.HueCommandNotExecutedException;

public class EventNotExecutedException extends HueCommandNotExecutedException {
    public EventNotExecutedException(String message) {
        super(message);
    }
}
