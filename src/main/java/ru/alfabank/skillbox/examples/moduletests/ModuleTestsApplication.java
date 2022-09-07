package ru.alfabank.skillbox.examples.moduletests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableFeignClients
@EnableWebSecurity
@SpringBootApplication
public class ModuleTestsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleTestsApplication.class, args);
    }

}
