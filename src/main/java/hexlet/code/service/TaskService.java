package hexlet.code.service;

import hexlet.code.dto.taskDTO.TaskCreateDTO;
import hexlet.code.dto.taskDTO.TaskDTO;
import hexlet.code.dto.taskDTO.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
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
    private LabelRepository labelRepository;

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

        var statusTask = taskStatusRepository.findBySlug(dto.getStatus())
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + dto.getStatus()));

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
