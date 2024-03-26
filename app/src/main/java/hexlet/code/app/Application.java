package hexlet.code.app;

import hexlet.code.app.component.DefaultUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    @Autowired
    private DefaultUser defaultUser;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
