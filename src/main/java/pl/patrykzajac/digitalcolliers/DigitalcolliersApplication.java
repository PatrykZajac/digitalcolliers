package pl.patrykzajac.digitalcolliers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.patrykzajac.digitalcolliers.configuration.ApplicationProperties;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@Slf4j
public class DigitalcolliersApplication {

    public static void main(String[] args) throws URISyntaxException, IOException {
        SpringApplication.run(DigitalcolliersApplication.class, args);
    }
}
