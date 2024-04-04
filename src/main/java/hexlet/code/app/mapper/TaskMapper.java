package hexlet.code.app.mapper;

import hexlet.code.app.dto.taskDTO.TaskCreateDTO;
import hexlet.code.app.dto.taskDTO.TaskDTO;
import hexlet.code.app.dto.taskDTO.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Mapping(target = "taskStatus.slug", source = "status")
    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "title", source = "name")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "name", source = "title")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
}
