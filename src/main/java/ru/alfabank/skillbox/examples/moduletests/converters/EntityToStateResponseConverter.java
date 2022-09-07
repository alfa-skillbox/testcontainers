package ru.alfabank.skillbox.examples.moduletests.converters;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.alfabank.skillbox.examples.moduletests.dto.JsonStateResponse;
import ru.alfabank.skillbox.examples.moduletests.persistance.JsonEntity;

@Component
public class EntityToStateResponseConverter {

    @NonNull
    public JsonStateResponse convert(JsonEntity entity) {
        return JsonStateResponse.builder()
                .json(entity.getJson())
                .isValid(true)
                .id(String.valueOf(entity.getId()))
                .build();
    }
}
