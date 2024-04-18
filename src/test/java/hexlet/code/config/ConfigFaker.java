package hexlet.code.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigFaker {
    @Bean
    public Faker initFaker() {
        return new Faker();
    }
}
