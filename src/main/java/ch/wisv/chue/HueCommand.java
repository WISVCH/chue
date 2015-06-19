package ch.wisv.chue;

import com.philips.lighting.model.PHBridge;

/**
 * Hue command interface
 */
public interface HueCommand {
    void execute(PHBridge bridge, String... lightIdentifiers);
}
