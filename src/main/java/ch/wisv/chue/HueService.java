package ch.wisv.chue;

import ch.wisv.chue.events.EventNotExecutedException;
import ch.wisv.chue.events.HueEvent;
import ch.wisv.chue.hue.BridgeUnavailableException;
import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.states.BlankState;
import ch.wisv.chue.states.HueState;
import ch.wisv.chue.states.StateNotLoadedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    /**
     * Load a state on the lamps
     *
     * @param state            the state to load
     * @param lamps the lamps to apply the state to
     */
    public void loadState(HueState state, Set<HueLamp> lamps) {
        if (!hueFacade.isBridgeAvailable()) {
            throw new StateNotLoadedException("Hue bridge is not available");
        }

        restoreState = () -> {
            new BlankState().execute(hueFacade, lamps);
            state.execute(hueFacade, lamps);
            log.debug("Light states restored!");
        };

        // Set everything to default before loading state.
        new BlankState().execute(hueFacade, lamps);
        state.execute(hueFacade, lamps);
    }

    /**
     * Load an event on the lamps
     *
     * @param event the event to load
     * @param duration the time the event may take
     * @param lamps the lamps to apply the event to
     */
    public void loadEvent(HueEvent event, int duration, Set<HueLamp> lamps) {
        if (!hueFacade.isBridgeAvailable()) {
            throw new EventNotExecutedException("Hue bridge is not available");
        }

        event.execute(hueFacade, lamps);

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
                new BlankState().execute(hueFacade, lamps);
        };
        new Thread(restore, "ServiceThread").start();
    }

    /**
     * Strobe the provided lamps for the specified time
     *
     * @param millis duration in milliseconds
     * @param lamps the lamps to strobe
     */
    public void strobe(int millis, Set<HueLamp> lamps) {
        try {
            hueFacade.strobe(millis, lamps);
        } catch (BridgeUnavailableException e) {
            throw new EventNotExecutedException(e);
        }
    }

    /**
     * Fetch a lamp by its identifier
     *
     * @param id the identifier
     * @return the Hue lamp
     */
    public HueLamp getLampById(String id) {
        return hueFacade.getAvailableLamps().get(id);
    }

    /**
     * @return set with all Hue lamps
     */
    public Set<HueLamp> getLamps() {
        return new HashSet<>(hueFacade.getAvailableLamps().values());
    }

    /**
     * Fetch multiple lamps using a list of identifiers
     *
     * @param ids the identifiers of the requested lamps
     * @return the set of lamps
     */
    public Set<HueLamp> getLampsById(List<String> ids) {
        return ids.stream()
                .map(id -> hueFacade.getAvailableLamps().get(id))
                .collect(Collectors.toSet());
    }

    /**
     * Set the Hue facade to use (for testing purposes)
     *
     * @param hueFacade the new Hue facade
     */
    protected void setHueFacade(HueFacade hueFacade) {
        this.hueFacade = hueFacade;
    }
}
