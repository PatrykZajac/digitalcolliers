package pl.patrykzajac.digitalcolliers.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private HashMap<String, String> security;
}
