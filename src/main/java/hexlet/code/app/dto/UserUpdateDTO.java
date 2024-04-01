package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class UserUpdateDTO {
    @NotBlank
    private JsonNullable<String> firstName;

    @NotBlank
    private JsonNullable<String> lastName;

    @NotNull
    private JsonNullable<String> email;

    @NotNull
    @Size(min = 1)
    private JsonNullable<String> password;
}
