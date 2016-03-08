package ch.wisv.chue;

import ch.wisv.chue.events.Alert;
import ch.wisv.chue.events.EventNotExecutedException;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.states.*;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * Spring MVC Web Controller
 */
@Controller
public class WebController {

    @Autowired
    HueService hue;

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(EventNotExecutedException.class)
    @ResponseBody
    String handleNotExecuted(EventNotExecutedException e) {
        return "The event was not executed: " + e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(StateNotLoadedException.class)
    @ResponseBody
    String handleNotExecuted(StateNotLoadedException e) {
        return "The state was not loaded: " + e.getMessage();
    }

    @RequestMapping("/")
    String index(Model model) {
        model.addAttribute("lights", hue.getLamps());
        return "index";
    }

    @RequestMapping("/random")
    @ResponseBody
    String random() {
        StringBuilder sb = new StringBuilder("Randomization complete: ");

        Set<HueLamp> lamps = hue.getLamps();
        hue.loadState(new RandomColorState(), lamps);

        sb.append(getPrettyColors(lamps));
        return sb.toString();
    }

    @RequestMapping("/random/{id}")
    @ResponseBody
    String random(@PathVariable String id) {
        StringBuilder sb = new StringBuilder("Randomization complete: ");

        HueLamp lamp = hue.getLampById(id);
        hue.loadState(new RandomColorState(), Collections.singleton(lamp));

        sb.append(getPrettyColor(lamp));
        return sb.toString();
    }

    @RequestMapping("/strobe/all")
    @ResponseBody
    String strobeAll(@RequestParam(value = "duration", defaultValue = "500") Integer duration) {
        hue.strobe(duration, hue.getLamps());
        return "Strobe for duration=" + duration + "ms";
    }

    @RequestMapping("/strobe")
    @ResponseBody
    String strobe(@RequestParam(value = "id[]") String[] id, @RequestParam(value = "duration", defaultValue = "500")
    Integer duration) {
        hue.strobe(duration, hue.getLampsById(Arrays.asList(id)));
        return "Strobe lamps (" + Arrays.asList(id) + ") for duration=" + duration + "ms";
    }

    @RequestMapping("/colorloop")
    @ResponseBody
    String colorLoop() {
        hue.loadState(new ColorLoopState(), hue.getLamps());
        return "Colorloop";
    }

    @RequestMapping("/colorloop/{id}")
    @ResponseBody
    String colorLoop(@PathVariable String id) {
        hue.loadState(new ColorLoopState(), Collections.singleton(hue.getLampById(id)));
        return "Colorloop";
    }

    @RequestMapping("/randomcolorloop")
    @ResponseBody
    String randomColorLoop() {
        hue.loadState(new RandomColorLoopState(), hue.getLamps());
        return "Random Colorloop";
    }

    @RequestMapping("/randomcolorloop/{id}")
    @ResponseBody
    String randomColorLoop(@PathVariable String id) {
        hue.loadState(new RandomColorLoopState(), Collections.singleton(hue.getLampById(id)));
        return "Random Colorloop";
    }

    @RequestMapping("/alert")
    @ResponseBody
    String alert(@RequestParam(value = "timeout", defaultValue = "5000") Integer timeout) {
        hue.loadEvent(new Alert(), timeout, hue.getLamps());
        return String.format("Alerting for %d milliseconds", timeout);
    }

    @RequestMapping("/alert/{id}")
    @ResponseBody
    String alert(@RequestParam(value = "timeout", defaultValue = "5000") Integer timeout, @PathVariable String id) {
        if ("all".equals(id)) {
            hue.loadEvent(new Alert(), timeout, hue.getLamps());
        } else {
            hue.loadEvent(new Alert(), timeout, Collections.singleton(hue.getLampById(id)));
        }

        return String.format("Alerting for %d milliseconds", timeout);
    }

    @RequestMapping({"/oranje", "/54"})
    @ResponseBody
    String oranje() {
        hue.loadState(new ColorState(Color.web("#FFA723")), hue.getLamps());
        return "B'voranje";
    }

    @RequestMapping(value = "/color/{id}/{hex:[a-fA-F0-9]{6}}", method = RequestMethod.GET)
    @ResponseBody
    String color(@PathVariable String id, @PathVariable String hex) {
        StringBuilder sb = new StringBuilder("Time for some new colours: ");

        Set<HueLamp> lamps;
        if ("all".equals(id)) {
            lamps = hue.getLamps();
        } else {
            lamps = Collections.singleton(hue.getLampById(id));
        }

        hue.loadState(new ColorState(Color.web('#' + hex)), lamps);
        sb.append(getPrettyColors(lamps));
        return sb.toString();
    }

    @RequestMapping(value = "/color/{id}/{colorName:(?![a-fA-F0-9]{6}).*}", method = RequestMethod.GET)
    @ResponseBody
    String colorFriendly(@PathVariable String id, @PathVariable String colorName) {
        StringBuilder sb = new StringBuilder("Time for some new colours: ");

        Set<HueLamp> lamps;
        if ("all".equals(id)) {
            lamps = hue.getLamps();
        } else {
            lamps = Collections.singleton(hue.getLampById(id));
        }

        hue.loadState(new ColorState(Color.valueOf(colorName)), lamps);
        sb.append(getPrettyColors(lamps));
        return sb.toString();
    }

    @RequestMapping(value = "/color", method = RequestMethod.POST)
    @ResponseBody
    String colorPost(@RequestParam(value = "id[]") String[] ids, @RequestParam String hex) {
        StringBuilder sb = new StringBuilder("Time for some new colours: ");

        Set<HueLamp> lamps = hue.getLampsById(Arrays.asList(ids));
        hue.loadState(new ColorState(Color.web(hex)), lamps);

        sb.append(getPrettyColors(lamps));
        return sb.toString();
    }

    /**
     * Converts a list of lamps to a pretty String with identifiers and hex colors
     *
     * @param lamps the set of lamps
     * @return pretty String with identifiers and hex values of the colors of the lamps
     */
    private static String getPrettyColors(Set<HueLamp> lamps) {
        StringBuilder sb = new StringBuilder();

        lamps.stream().forEach(l -> sb.append(getPrettyColor(l)).append(", "));
        sb.delete(sb.length() - 2, sb.length());

        return sb.toString();
    }

    /**
     * Converts a lamps to a pretty String with identifier and hex color
     *
     * @param lamp the lamp
     * @return pretty String with identifier and hex value of the color of the lamp
     */
    private static String getPrettyColor(HueLamp lamp) {
        if (lamp == null || !lamp.getLastState().isPresent() || !lamp.getLastState().get().getColor().isPresent()) {
            return "undefined lamp or colour";
        }

        Color c = lamp.getLastState().get().getColor().get();

        return String.format("lamp %s is now #%02x%02x%02x",
                lamp.getId(),
                (int) Math.round(c.getRed() * 255.0),
                (int) Math.round(c.getGreen() * 255.0),
                (int) Math.round(c.getBlue() * 255.0));
    }


}
