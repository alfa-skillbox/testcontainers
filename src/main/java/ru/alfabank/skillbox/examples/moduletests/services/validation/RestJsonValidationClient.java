package ru.alfabank.skillbox.examples.moduletests.services.validation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.alfabank.skillbox.examples.moduletests.config.RestJsonFeignClientConfiguration;

@FeignClient(name = "${feign.clients.rest-json-validation.name}",
        url = "${feign.clients.rest-json-validation.url}",
configuration = RestJsonFeignClientConfiguration.class)
public interface RestJsonValidationClient {

    @PostMapping(path = "${feign.clients.rest-json-validation.path}")
    JsonValidationResponse validate(@RequestBody String json);
}
