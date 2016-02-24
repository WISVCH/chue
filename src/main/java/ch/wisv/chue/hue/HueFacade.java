package ch.wisv.chue.hue;

import java.util.List;

public interface HueFacade {
    /**
     * Strobe the specified lights for the specified time.
     * Implemented on the facade because of the low-level operations.
     *
     * @param millis           duration in milliseconds
     * @param lightIdentifiers the lights to strobe
     * @see <a href="http://www.lmeijer.nl/archives/225-Do-hue-want-a-strobe-up-there.html">Strobe with Hue by Leon
     * Meijer</a>
     */
    void strobe(int millis, String... lightIdentifiers) throws BridgeUnavailableException;

    /**
     * @return list with String ids of all lights
     */
    List<HueLamp> getAvailableLamps();

    /**
     * @return true iff the bridge is available
     */
    boolean bridgeAvailable();

    /**
     * Sets the light state of the lamps
     *
     * @param id the identifier of the light
     * @param lightState the new state
     */
    void updateLightState(String id, HueLightState lightState) throws BridgeUnavailableException;
}
