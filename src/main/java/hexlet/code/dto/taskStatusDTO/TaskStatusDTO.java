package hexlet.code.dto.taskStatusDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusDTO {
    private Long id;
    private String name;
    private String slug;
    private String createdAt;
}
