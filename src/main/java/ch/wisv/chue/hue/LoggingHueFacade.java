package ch.wisv.chue.hue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class LoggingHueFacade implements HueFacade {
    private static final Logger log = LoggerFactory.getLogger(LoggingHueFacade.class);

    @Override
    public void strobe(int millis, String... lightIdentifiers) {
        log.info("Strobing " + Arrays.asList(lightIdentifiers) + " for " + millis + " ms.");
    }

    @Override
    public List<HueLamp> getAllLamps() {
        return Arrays.asList(new HueLamp("1", "Dummy 1"), new HueLamp("2", "Dummy 2"));
    }

    @Override
    public void updateLightState(String id, HueLightState lightState) {
        log.info("Updating " + id + " to " + lightState);
    }
}
