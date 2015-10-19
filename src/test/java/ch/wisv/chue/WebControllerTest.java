package ch.wisv.chue;

import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.HueLamp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebControllerTest {
    @Mock
    private HueFacade hueFacade;

    @Spy
    private HueService hueService = new HueService();

    @InjectMocks
    private WebController webController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(hueFacade.getAllLamps()).thenReturn(
                Arrays.asList(new HueLamp("1", "Lamp 1"), new HueLamp("2", "Lamp 2"), new HueLamp("3", "Lamp 3")));
        hueService.setHueFacade(hueFacade);

        mockMvc = MockMvcBuilders.standaloneSetup(webController).build();
    }

    @Test
    public void testAlertAll() throws Exception {
        mockMvc.perform(get("/alert/all"))
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
        mockMvc.perform(get("/alert/all")
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
        mockMvc.perform(get("/color/all/ff0000"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        is("Time for some new colours: lamp 1 is now #ff0000, lamp 2 is now #ff0000, lamp 3 is now #ff0000")));
    }

    @Test
    public void testColorHexID() throws Exception {
        mockMvc.perform(get("/color/1/0000ff"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        is("Time for some new colours: lamp 1 is now #0000ff")));
    }

    @Test
    public void testColorFriendlyAll() throws Exception {
        mockMvc.perform(get("/color/all/red"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        is("Time for some new colours: lamp 1 is now #ff0000, lamp 2 is now #ff0000, lamp 3 is now #ff0000")));
    }

    @Test
    public void testColorFriendlyID() throws Exception {
        mockMvc.perform(get("/color/2/blue"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        is("Time for some new colours: lamp 2 is now #0000ff")));
    }
}
