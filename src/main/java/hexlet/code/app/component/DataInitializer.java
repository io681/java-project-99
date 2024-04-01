package hexlet.code.app.component;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.UserService;
import lombok.AllArgsConstructor;
import org.assertj.core.util.introspection.CaseFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    private final String[] initialSlugsTaskStatus =
            {"draft", "to_review", "to_be_fixed", "to_publish", "published"};

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createAdminUser();
        initTaskStatuses();
    }

    private void createAdminUser() {
        var userAdmin = new User();
        userAdmin.setEmail("hexlet@example.com");
        userAdmin.setPasswordDigest("qwerty");
        userService.createUser(userAdmin);
    }

    private void initTaskStatuses() {
        var result = Arrays.stream(initialSlugsTaskStatus)
                .map(n -> {
                    var taskStatus = new TaskStatus();
                    taskStatus.setName(CaseFormatUtils.toCamelCase(n));
                    taskStatus.setSlug(n);
                    return taskStatus;
                })
                .toList();

        result.forEach(taskStatusRepository::save);
    }
}
