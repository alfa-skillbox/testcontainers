package ru.alfabank.skillbox.examples.moduletests.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.io.IOException;

@TestConfiguration
public class IntegTestSchemaResourceConfig {

    @Value("classpath:schema-integtest.json")
    private Resource schema;

    @Bean
    @Primary
    @Profile("integtest")
    public ObjectNode getSchema() throws IOException {
        return new ObjectMapper().readValue(FileUtils.readFileToString(schema.getFile()), ObjectNode.class);
    }
}
