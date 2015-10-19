package ch.wisv.chue;

import ch.wisv.chue.events.Alert;
import ch.wisv.chue.states.ColorLoopState;
import ch.wisv.chue.states.ColorState;
import ch.wisv.chue.states.RandomColorLoopState;
import ch.wisv.chue.states.RandomColorState;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Spring MVC Web Controller
 */
@Controller
public class WebController {

    @Autowired
    HueService hue;

    @RequestMapping("/")
    String index(Model model) {
        model.addAttribute("lights", hue.getAllLights());
        return "index";
    }

    @RequestMapping("/random")
    @ResponseBody
    String random() {
        RandomColorState randomColorState = new RandomColorState();
        hue.loadState(randomColorState);

        return "Randomization complete: " + getPrettyLightColors(randomColorState.getLightColors());
    }

    @RequestMapping("/random/{id}")
    @ResponseBody
    String random(@PathVariable String id) {
        RandomColorState randomColorState = new RandomColorState();
        hue.loadState(randomColorState, id);

        return "Randomization complete: " + getPrettyLightColors(randomColorState.getLightColors());
    }

    @RequestMapping("/strobe/all")
    @ResponseBody
    String strobeAll(@RequestParam(value = "duration", defaultValue = "500") Integer duration) {
        hue.strobe(duration);
        return "Strobe for duration=" + duration + "ms";
    }

    @RequestMapping("/strobe")
    @ResponseBody
    String strobe(@RequestParam(value = "id[]") String[] id, @RequestParam(value = "duration", defaultValue = "500")
    Integer duration) {
        hue.strobe(duration, id);
        return "Strobe lamps (" + Arrays.asList(id) + ") for duration=" + duration + "ms";
    }

    @RequestMapping("/colorloop")
    @ResponseBody
    String colorLoop() {
        hue.loadState(new ColorLoopState());
        return "Colorloop";
    }

    @RequestMapping("/colorloop/{id}")
    @ResponseBody
    String colorLoop(@PathVariable String id) {
        hue.loadState(new ColorLoopState(), id);
        return "Colorloop";
    }

    @RequestMapping("/randomcolorloop")
    @ResponseBody
    String randomColorLoop() {
        hue.loadState(new RandomColorLoopState());
        return "Random Colorloop";
    }

    @RequestMapping("/randomcolorloop/{id}")
    @ResponseBody
    String randomColorLoop(@PathVariable String id) {
        hue.loadState(new RandomColorLoopState(), id);
        return "Random Colorloop";
    }

    @RequestMapping("/alert")
    @ResponseBody
    String alert(@RequestParam(value = "timeout", defaultValue = "5000") Integer timeout) {
        hue.loadEvent(new Alert(), timeout);
        return String.format("Alerting for %d milliseconds", timeout);
    }

    @RequestMapping("/alert/{id}")
    @ResponseBody
    String alert(@RequestParam(value = "timeout", defaultValue = "5000") Integer timeout, @PathVariable String id) {
        hue.loadEvent(new Alert(), timeout, id);
        return String.format("Alerting for %d milliseconds", timeout);
    }

    @RequestMapping({"/oranje", "/54"})
    @ResponseBody
    String oranje() {
        hue.loadState(new ColorState(Color.web("#FFA723")));
        return "B'voranje";
    }

    @RequestMapping(value = "/color/{id}/{hex:[a-fA-F0-9]{6}}", method = RequestMethod.GET)
    @ResponseBody
    String color(@PathVariable String id, @PathVariable String hex) {
        Color color = Color.web('#' + hex);

        hue.loadState(new ColorState(color), id);

        return "Changed colour of lamps (" + id + ") to #" + hex;
    }

    @RequestMapping(value = "/color/{id}/{colorName:(?![a-fA-F0-9]{6}).*}", method = RequestMethod.GET)
    @ResponseBody
    String colorFriendly(@PathVariable String id, @PathVariable String colorName) {
        Color color = Color.valueOf(colorName);

        hue.loadState(new ColorState(color), id);

        String hex = String.format("%02x%02x%02x",
                (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));

        return "Changed colour of lamps (" + id + ") to #" + hex;
    }

    @RequestMapping(value = "/color", method = RequestMethod.POST)
    @ResponseBody
    String colorPost(@RequestParam(value = "id[]") String[] id, @RequestParam String hex) {
        hue.loadState(new ColorState(Color.web(hex)), id);

        return "Changed colour of lamps (" + Arrays.asList(id) + ") to " + hex;
    }

    /**
     * Converts a map of lamps and their colors to a pretty String.
     *
     * @param affectedLights the map containing the affected lamps
     * @return pretty String with hex values for all affected lamps
     */
    private static String getPrettyLightColors(Map<String, Color> affectedLights) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Color> light : affectedLights.entrySet()) {
            sb.append("lamp ")
                    .append(light.getKey())
                    .append(" is now ")
                    .append(String.format("#%02x%02x%02x",
                            (int) (light.getValue().getRed() * 255),
                            (int) (light.getValue().getGreen() * 255),
                            (int) (light.getValue().getBlue() * 255)))
                    .append(",");
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
