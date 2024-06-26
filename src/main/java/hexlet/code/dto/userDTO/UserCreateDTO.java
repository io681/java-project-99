package hexlet.code.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {
    private String firstName;
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 3)
    private String password;

}
