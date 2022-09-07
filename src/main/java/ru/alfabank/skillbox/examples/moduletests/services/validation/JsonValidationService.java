package ru.alfabank.skillbox.examples.moduletests.services.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface JsonValidationService {

    JsonValidationResponse validate(String json);

    @Service
    @RequiredArgsConstructor
    class DefaultJsonValidationService implements JsonValidationService {

        private static final ObjectMapper MAPPER = new ObjectMapper();

        private final RestJsonValidationClient client;

        private final ObjectNode schema;

        @Override
        public JsonValidationResponse validate(String json) {
            try {
                var request = formRequest(json);
                return client.validate(MAPPER.writeValueAsString(request));
            } catch (NotFoundDecodeException nfe) {
                return nfe.getJsonValidationResponse();
            } catch (Exception e) {
                return formExceptionalResponse(e);
            }
        }

        private JsonValidationResponse formExceptionalResponse(Exception e) {
            return JsonValidationResponse.builder()
                    .errors(List.of(e.getMessage()))
                    .build();
        }

        private JsonValidationRequest formRequest(String json) {
            return JsonValidationRequest.builder()
                    .json(json)
                    .schema(schema)
                    .build();
        }
    }
}
