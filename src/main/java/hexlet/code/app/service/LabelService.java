package hexlet.code.app.service;

import hexlet.code.app.dto.labelDTO.LabelCreateDTO;
import hexlet.code.app.dto.labelDTO.LabelDTO;
import hexlet.code.app.dto.labelDTO.LabelUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

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
        labelRepository.deleteById(id);
    }
}
