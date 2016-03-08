package ch.wisv.chue.hue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class LoggingHueFacade implements HueFacade {
    private static final Logger log = LoggerFactory.getLogger(LoggingHueFacade.class);

    private SortedMap<String, HueLamp> lamps = new TreeMap<>();

    @PostConstruct
    public void init() {
        lamps.put("1", new HueLamp("1", "Dummy 1"));
        lamps.put("2", new HueLamp("2", "Dummy 2"));
    }

    @Override
    public void strobe(int millis, Set<HueLamp> lamps) throws BridgeUnavailableException {
        log.info("Strobing " + lamps + " for " + millis + " ms.");
    }

    @Override
    public SortedMap<String, HueLamp> getAvailableLamps() {
        return lamps;
    }

    @Override
    public boolean isBridgeAvailable() {
        // The theoretical bridge is available (otherwise this facade doesn't make sense)
        return true;
    }

    @Override
    public void updateLightState(HueLamp lamp, HueLightState lightState) throws BridgeUnavailableException {
        if (null == lamp) {
            log.warn("The lamp is null!");
            return;
        }

        log.info("Updating " + lamp + " to " + lightState);
        lamp.setLastState(lightState);
    }
}
