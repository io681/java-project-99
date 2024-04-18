package hexlet.code.service;

import hexlet.code.dto.taskStatusDTO.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatusDTO.TaskStatusDTO;
import hexlet.code.dto.taskStatusDTO.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.UnprocessableEntityException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> index() {
        var taskStatuses = taskStatusRepository.findAll();

        return taskStatuses.stream()
                .map(taskStatusMapper::map)
                .toList();
    }

    public TaskStatusDTO show(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return taskStatusMapper.map(taskStatus);

    }

    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        var taskStatus = taskStatusMapper.map(dto);
        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO dto, Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        taskStatusMapper.update(dto, taskStatus);
        taskStatusRepository.save(taskStatus);

        return taskStatusMapper.map(taskStatus);
    }

    public void delete(Long id) {
        taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        if (!taskRepository.findAllByTaskStatusIsNotNull().isEmpty()) {
            throw new UnprocessableEntityException("TaskStatus : " + id + " linked to task");
        }

        taskStatusRepository.deleteById(id);
    }
}
