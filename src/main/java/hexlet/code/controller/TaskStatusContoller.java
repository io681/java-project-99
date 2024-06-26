package hexlet.code.controller;

import hexlet.code.dto.taskStatusDTO.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatusDTO.TaskStatusDTO;
import hexlet.code.dto.taskStatusDTO.TaskStatusUpdateDTO;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@SecurityRequirement(name = "Authorization")
public class TaskStatusContoller {
    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping(path = "")
    public ResponseEntity<List<TaskStatusDTO>> index() {
        var result = taskStatusService.index();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public TaskStatusDTO show(@PathVariable Long id) {
        return taskStatusService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO dto) {
        return taskStatusService.create(dto);
    }

    @PutMapping(path = "/{id}")
    public TaskStatusDTO update(@Valid @RequestBody TaskStatusUpdateDTO dto, @PathVariable Long id) {
        return taskStatusService.update(dto, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskStatusService.delete(id);
    }
}
