package hexlet.code.app.dto.labelDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LabelDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}