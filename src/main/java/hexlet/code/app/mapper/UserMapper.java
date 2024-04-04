package hexlet.code.app.mapper;

import hexlet.code.app.dto.userDTO.UserCreateDTO;
import hexlet.code.app.dto.userDTO.UserDTO;
import hexlet.code.app.dto.userDTO.UserUpdateDTO;
import hexlet.code.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        // Подключение JsonNullableMapper
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(source = "password", target = "passwordDigest")
    public abstract User map(UserCreateDTO dto);

    public abstract UserDTO map(User model);

    @Mapping(source = "password", target = "passwordDigest")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(passwordEncoder.encode(password));
    }
}
