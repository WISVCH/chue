package ch.wisv.chue.hue;

import java.util.SortedMap;

public interface HueFacade {
    /**
     * @return map of all lamps, with identifiers pointing to the corresponding Hue lamp
     */
    SortedMap<String, HueLamp> getAvailableLamps();

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
