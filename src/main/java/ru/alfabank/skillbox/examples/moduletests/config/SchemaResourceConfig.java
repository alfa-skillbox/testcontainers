package ru.alfabank.skillbox.examples.moduletests.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class SchemaResourceConfig {

    @Value("classpath:schema.json")
    private Resource schema;

    @Bean
    public ObjectNode getSchema() throws IOException {
        return new ObjectMapper().readValue(FileUtils.readFileToString(schema.getFile()), ObjectNode.class);
    }
}
