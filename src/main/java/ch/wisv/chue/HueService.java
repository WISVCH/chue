package ch.wisv.chue;

import ch.wisv.chue.events.HueEvent;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.states.BlankState;
import ch.wisv.chue.states.HueState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HueService {

    private static final Logger log = LoggerFactory.getLogger(HueService.class);

    @Autowired
    private HueFacade hueFacade;

    public interface Command {
        void execute();
    }

    private Command restoreState;

    private String[] getLightIdentifiers(String... lightIdentifiers) {
        if (lightIdentifiers.length == 0 || "all".equals(lightIdentifiers[0])) {
            List<String> ids = hueFacade.getAllLamps().stream().map(HueLamp::getId).collect(Collectors.toList());
            return ids.toArray(new String[ids.size()]);
        } else {
            return lightIdentifiers;
        }
    }

    /**
     * Load a state on the lights
     *
     * @param state            the state to load
     * @param lightIdentifiers the lights to apply the state to
     */
    public void loadState(HueState state, String... lightIdentifiers) {
        String[] ids = getLightIdentifiers(lightIdentifiers);
        restoreState = () -> {
            new BlankState().execute(hueFacade, ids);
            state.execute(hueFacade, ids);
            log.debug("Light states restored!");
        };

        // Set everything to default before loading state.
        new BlankState().execute(hueFacade, ids);
        state.execute(hueFacade, ids);
    }

    /**
     * Load an event on the lights
     *
     * @param event the event to load
     * @param duration the time the event may take
     * @param lightIdentifiers the lights to apply the event to
     */
    public void loadEvent(HueEvent event, int duration, String... lightIdentifiers) {
        String[] ids = getLightIdentifiers(lightIdentifiers);
        event.execute(hueFacade, ids);

        Runnable restore = () -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                log.warn("Interrupted, not restoring light states! Exception: " + e.getMessage());
            }
            log.debug("Restoring light states after event.");
            if (this.restoreState != null)
                this.restoreState.execute();
            else
                new BlankState().execute(hueFacade, lightIdentifiers);
        };
        new Thread(restore, "ServiceThread").start();
    }

    /**
     * Strobe the specified lights for the specified time
     *
     * @param millis duration in milliseconds
     * @param lightIdentifiers the lights to strobe
     */
    public void strobe(int millis, String... lightIdentifiers) {
        hueFacade.strobe(millis, getLightIdentifiers(lightIdentifiers));
    }

    /**
     * @return list with all hue lamps
     */
    public List<HueLamp> getAllLamps() {
        return hueFacade.getAllLamps();
    }

    /**
     * Set the hue facade to use (for testing purposes)
     *
     * @param hueFacade the new hue facade
     */
    protected void setHueFacade(HueFacade hueFacade) {
        this.hueFacade = hueFacade;
    }
}
