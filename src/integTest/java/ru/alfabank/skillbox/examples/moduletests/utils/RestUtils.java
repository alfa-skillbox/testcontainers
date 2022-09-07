package ru.alfabank.skillbox.examples.moduletests.utils;


import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public abstract class RestUtils {

    public static void validationRestStub(HttpStatus status, String responsePath) {
        MappingBuilder mappingBuilder = WireMock.post(WireMock.urlEqualTo("/json"));
        ResponseDefinitionBuilder responseDefinitionBuilder = aResponse()
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withStatus(status.value())
                .withBodyFile(responsePath);
        WireMock.givenThat(mappingBuilder.willReturn(responseDefinitionBuilder));
    }
}