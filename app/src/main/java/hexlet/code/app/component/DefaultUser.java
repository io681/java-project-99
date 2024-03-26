package hexlet.code.app.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user")
@Getter
@Setter
public class DefaultUser {
    private String username;
    private String password;
}
