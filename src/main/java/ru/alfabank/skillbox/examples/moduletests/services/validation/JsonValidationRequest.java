package ru.alfabank.skillbox.examples.moduletests.services.validation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class JsonValidationRequest implements Serializable {
    private ObjectNode schema;
    private String json;
}