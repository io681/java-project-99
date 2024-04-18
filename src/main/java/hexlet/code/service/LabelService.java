package hexlet.code.service;

import hexlet.code.dto.labelDTO.LabelCreateDTO;
import hexlet.code.dto.labelDTO.LabelDTO;
import hexlet.code.dto.labelDTO.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.UnprocessableEntityException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> index() {
        var labels = labelRepository.findAll();

        return labels.stream()
                .map(labelMapper::map)
                .toList();
    }

    public LabelDTO show(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return labelMapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO dto) {
        var label = labelMapper.map(dto);

        labelRepository.save(label);

        return labelMapper.map(label);
    }

    public LabelDTO update(LabelUpdateDTO dto, Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        labelMapper.update(dto, label);
        labelRepository.save(label);

        return labelMapper.map(label);
    }

    public void delete(Long id) {
        labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        if (!taskRepository.findAllByLabelsIsNotNull().isEmpty()) {
            throw new UnprocessableEntityException("Label : " + id + " linked to task");
        }

        labelRepository.deleteById(id);
    }
}
