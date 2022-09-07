package ru.alfabank.skillbox.examples.moduletests.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public abstract class TestUtils {

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    public static String classpathFileToString(String filePath) {
        return FileUtils.readFileToString(
                new File(TestUtils.class.getResource(filePath).getPath()), StandardCharsets.UTF_8.name()
        );
    }

    @SneakyThrows
    public static <T> T classpathFileToObject(String filePath,  Class<T> clazz) {
        return MAPPER.readValue(new FileInputStream(TestUtils.class.getResource(filePath).getPath()), clazz);
    }

    @SneakyThrows
    public static String objectToString(Object object) {
        return MAPPER.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T stringToObject(String str, Class<T> clazz) {
        return MAPPER.readValue(str, clazz);
    }
}