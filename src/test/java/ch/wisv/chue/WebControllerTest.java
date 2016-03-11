package ch.wisv.chue;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import ch.wisv.chue.hue.LoggingHueFacade;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebControllerTest {
    @Spy
    private HueFacade hueFacade = new LoggingHueFacade();

    @Spy
    private HueService hueService = new HueService();

    @InjectMocks
    private WebController webController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        SortedMap<String, HueLamp> lamps = new TreeMap<>();
        lamps.put("1", new HueLamp("1", "Lamp 1"));
        lamps.put("2", new HueLamp("2", "Lamp 2"));
        lamps.put("3", new HueLamp("3", "Lamp 3"));

        when(hueFacade.isBridgeAvailable()).thenReturn(true);
        when(hueFacade.getAvailableLamps()).thenReturn(lamps);
        hueService.setHueFacade(hueFacade);

        mockMvc = MockMvcBuilders.standaloneSetup(webController).build();
    }

    @Test
    public void testEventFailBridgeUnavailable() throws Exception {
        when(hueFacade.isBridgeAvailable()).thenReturn(false);
        when(hueFacade.getAvailableLamps()).thenReturn(Collections.emptySortedMap());

        mockMvc.perform(get("/alert"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string(is("The event was not executed: Hue bridge is not available")));
    }

    @Test
    public void testStateFailBridgeUnavailable() throws Exception {
        when(hueFacade.isBridgeAvailable()).thenReturn(false);
        when(hueFacade.getAvailableLamps()).thenReturn(Collections.emptySortedMap());

        mockMvc.perform(get("/random"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string(is("The state was not loaded: Hue bridge is not available")));
    }

    @Test
    public void testAlertAll() throws Exception {
        mockMvc.perform(get("/alert"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("Alerting for 5000 milliseconds")));
    }

    @Test
    public void testAlertSingle() throws Exception {
        mockMvc.perform(get("/alert/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("Alerting for 5000 milliseconds")));
    }

    @Test
    public void testAlertAllTimeOut() throws Exception {
        mockMvc.perform(get("/alert")
                .param("timeout", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("Alerting for 100 milliseconds")));
    }

    @Test
    public void testOranje() throws Exception {
        mockMvc.perform(get("/oranje"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("B'voranje")));
    }

    @Test
    public void testColorHexAll() throws Exception {
        mockMvc.perform(get("/color/ff0000"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        is("Time for some new colours: lamp 1 is now #ff0000, lamp 2 is now #ff0000, lamp 3 is now #ff0000")));
    }

    @Test
    public void testColorHexID() throws Exception {
        mockMvc.perform(get("/color/0000ff/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        is("Time for some new colours: lamp 1 is now #0000ff")));
    }

    @Test
    public void testColorFriendlyAll() throws Exception {
        mockMvc.perform(get("/color/red"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        is("Time for some new colours: lamp 1 is now #ff0000, lamp 2 is now #ff0000, lamp 3 is now #ff0000")));
    }

    @Test
    public void testColorFriendlyID() throws Exception {
        mockMvc.perform(get("/color/blue/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        is("Time for some new colours: lamp 2 is now #0000ff")));
    }
}
