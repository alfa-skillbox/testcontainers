package ru.alfabank.skillbox.examples.moduletests.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import ru.alfabank.skillbox.examples.moduletests.services.validation.RestJsonFeignClientErrorDecoder;

public class RestJsonFeignClientConfiguration {

    @Bean
    public ErrorDecoder getErrorDecoder() {
        return new RestJsonFeignClientErrorDecoder();
    }
}
