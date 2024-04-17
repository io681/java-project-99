package hexlet.code.app;

import hexlet.code.app.component.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AppApplication {

    @Autowired
    private DataInitializer dataInitializer;
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
