package ch.wisv.chue;


import ch.wisv.chue.hue.HueFacade;
import ch.wisv.chue.hue.LoggingHueFacade;
import ch.wisv.chue.hue.PhilipsHueFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Profile("!dev")
    @Bean
    public HueFacade philipsHueFacade() {
        return new PhilipsHueFacade();
    }

    @Profile("dev")
    @Bean
    public HueFacade dummyHueFacade() {
        return new LoggingHueFacade();
    }
}
