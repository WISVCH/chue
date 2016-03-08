package ch.wisv.chue.hue;

import java.util.Map;
import java.util.Set;

public interface HueFacade {
    /**
     * Strobe the provided lamps for the specified time.
     * Implemented on the facade because of the low-level operations.
     *
     * @param millis           duration in milliseconds
     * @param lamps the lamps to strobe
     * @see <a href="http://www.lmeijer.nl/archives/225-Do-hue-want-a-strobe-up-there.html">Strobe with Hue by Leon
     * Meijer</a>
     * @throws BridgeUnavailableException iff the bridge is not available
     */
    void strobe(int millis, Set<HueLamp> lamps) throws BridgeUnavailableException;

    /**
     * @return map of all lamps, with identifiers pointing to the corresponding Hue lamp
     */
    Map<String, HueLamp> getAvailableLamps();

    /**
     * @return true iff the bridge is available
     */
    boolean isBridgeAvailable();

    /**
     * Sets the light state of the lamps
     *
     * @param lamp the lamp that should be updated
     * @param lightState the new state
     * @throws BridgeUnavailableException iff the bridge is not available
     */
    void updateLightState(HueLamp lamp, HueLightState lightState) throws BridgeUnavailableException;
}
