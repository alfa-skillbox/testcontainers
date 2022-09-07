package ru.alfabank.skillbox.examples.moduletests;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.alfabank.skillbox.examples.moduletests.config.PermitAllResourceServerWebSecurityConfig;
import ru.alfabank.skillbox.examples.moduletests.dto.JsonStateResponse;
import ru.alfabank.skillbox.examples.moduletests.persistance.JsonEntity;
import ru.alfabank.skillbox.examples.moduletests.persistance.JsonRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alfabank.skillbox.examples.moduletests.utils.RestUtils.validationRestStub;
import static ru.alfabank.skillbox.examples.moduletests.utils.TestUtils.classpathFileToObject;
import static ru.alfabank.skillbox.examples.moduletests.utils.TestUtils.classpathFileToString;

// MOCKMVC (1)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("integTest-lesson2")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// (3) KEYCLOAK Отключаем проверку токенов
@Import({PermitAllResourceServerWebSecurityConfig.class})
public class ModuleTest_MockMvc {

    // MOCKMVC (1)
    @Autowired
    private MockMvc mockMvc;

    // (5) PostgreSQL мокаем взаимодействие с БД
    @MockBean
    private JsonRepository repository;

    // (4) REST мокаем взаимодействие с REST
    @BeforeEach
    void initStubs() {
        WireMock.reset();
        validationRestStub(HttpStatus.OK,
                "lesson2/responses/validate_OK.json");
    }

    @Test
    void applicationContextStartedSuccessfullyTest(ApplicationContext context) throws Exception {
        assertThat(context).isNotNull();
        // health-check
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void saveSuccessfullyTest() throws Exception {
        // when
        Long fakeId = 345L;
        String json = classpathFileToString("/mvc-requests/lesson2/save.json");
        when(repository.save(any(JsonEntity.class))).thenReturn(new JsonEntity(fakeId, json));
        JsonStateResponse expected = classpathFileToObject("/mvc-responses/lesson2/save_Ok.json", JsonStateResponse.class);
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/json/save")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(jsonPath("$.json", Matchers.equalTo(expected.getJson())))
                .andExpect(jsonPath("$.id", Matchers.equalTo(fakeId.toString())))
                .andExpect(jsonPath("$.valid", Matchers.equalTo(expected.isValid())));
    }

    @Test
    void saveWithoutJsonTest() throws Exception {
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/json/save")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
