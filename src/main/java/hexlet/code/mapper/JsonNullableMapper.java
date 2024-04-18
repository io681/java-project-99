package hexlet.code.mapper;


import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

//универсальный маппер, который можно подключить к любым другим мапперам c помощью  аннотации @Mapper
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public class JsonNullableMapper {

    public final <T> JsonNullable<T> wrap(T entity) {
        return JsonNullable.of(entity);
    }

    public final <T> T unwrap(JsonNullable<T> jsonNullable) {
        return jsonNullable == null ? null : jsonNullable.orElse(null);
    }

    @Condition
    public final <T> boolean isPresent(JsonNullable<T> nullable) {
        return nullable != null && nullable.isPresent();
    }
}
