package ch.wisv.chue;

import ch.wisv.chue.hue.HueFacade;

/**
 * Hue command interface
 */
public interface HueCommand {
    void execute(HueFacade hueFacade, String... lightIdentifiers);
}
