package hexlet.code.app.mapper;

import hexlet.code.app.dto.taskDTO.TaskCreateDTO;
import hexlet.code.app.dto.taskDTO.TaskDTO;
import hexlet.code.app.dto.taskDTO.TaskUpdateDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.LabelRepository;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "taskStatus.slug", source = "status")
    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "labels", source = "labels", qualifiedByName = "labelsNamesToLabels")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "labels", source = "labels", qualifiedByName = "labelsToLabelsNames")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "labels", source = "labels", qualifiedByName = "labelsNamesToLabels")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);


    @Named("labelsToLabelsNames")
    public List<String> labelsToLabelsNames(List<Label> labels) {
        return labels.isEmpty() ? new ArrayList<String>() : labels.stream()
                .map(Label::getName)
                .collect(Collectors.toList());
    }

    @Named("labelsNamesToLabels")
    public List<Label> labelsNamesToLabels(List<String> labelsNames) {
        return labelsNames.isEmpty() ? new ArrayList<Label>() : labelsNames.stream()
                .map(name -> labelRepository.findByName(name).get())
                .collect(Collectors.toList());
    }
}
