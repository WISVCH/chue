package ch.wisv.chue.hue;

import com.philips.lighting.model.PHBridge;

import java.util.List;

public interface HueFacade {
    int MAX_BRIGHTNESS = 254;
    int MAX_HUE = 65535;
    int MAX_SATURATION = 254;

    /**
     * Strobe the specified lights for the specified time.
     * Implemented on the facade because of the low-level operations.
     *
     * @param millis           duration in milliseconds
     * @param lightIdentifiers the lights to strobe
     * @see <a href="http://www.lmeijer.nl/archives/225-Do-hue-want-a-strobe-up-there.html">Strobe with Hue by Leon
     * Meijer</a>
     */
    void strobe(int millis, String... lightIdentifiers);

    /**
     * @return list with String ids of all lights
     */
    List<String> getAllLightIdentifiers();

    /**
     * @return the bridge
     */
    PHBridge getBridge();
}
