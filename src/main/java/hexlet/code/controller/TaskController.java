package hexlet.code.controller;

import hexlet.code.dto.taskDTO.TaskCreateDTO;
import hexlet.code.dto.taskDTO.TaskDTO;
import hexlet.code.dto.taskDTO.TaskParamsDTO;
import hexlet.code.dto.taskDTO.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import hexlet.code.specification.TaskSpecification;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "Authorization")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskSpecification taskSpecification;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @GetMapping(path = "")
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO params) {
        Specification<Task> spec = taskSpecification.build(params);
        var result = taskRepository.findAll(spec)
                .stream()
                .map((taskMapper::map))
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public TaskDTO show(@PathVariable Long id) {
        return taskService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO dto) {
        return taskService.create(dto);
    }

    @PutMapping(path = "/{id}")
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO dto, @PathVariable Long id) {
        return taskService.update(dto, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
