package hexlet.code.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Authorization")
public class WelcomeController {

    @GetMapping(path = "/welcome")
    public final String welcome() {
        return "Welcome to Spring";
    }
}
