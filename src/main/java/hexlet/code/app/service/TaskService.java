package hexlet.code.app.service;

import hexlet.code.app.dto.taskDTO.TaskCreateDTO;
import hexlet.code.app.dto.taskDTO.TaskDTO;
import hexlet.code.app.dto.taskDTO.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskDTO> index() {
        var tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO show(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return taskMapper.map(task);

    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);

        var statusTask = taskStatusRepository.findBySlug(dto.getStatus()).get();
        task.setTaskStatus(statusTask);

        if (dto.getAssigneeId() != null) {
            var userAssignee = userRepository.findById(dto.getAssigneeId()).get();
            task.setAssignee(userAssignee);
        }

        taskRepository.save(task);

        return taskMapper.map(task);
    }

    public TaskDTO update(TaskUpdateDTO dto, Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        taskMapper.update(dto, task);
        taskRepository.save(task);

        return taskMapper.map(task);
    }

    public void delete(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        taskRepository.deleteById(id);
    }
}
