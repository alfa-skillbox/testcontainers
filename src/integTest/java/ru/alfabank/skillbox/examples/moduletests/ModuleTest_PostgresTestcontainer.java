package ru.alfabank.skillbox.examples.moduletests;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alfabank.skillbox.examples.moduletests.config.PermitAllResourceServerWebSecurityConfig;
import ru.alfabank.skillbox.examples.moduletests.containers.PostgresContainerWrapper;
import ru.alfabank.skillbox.examples.moduletests.dto.JsonStateResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alfabank.skillbox.examples.moduletests.utils.RestUtils.validationRestStub;
import static ru.alfabank.skillbox.examples.moduletests.utils.TestUtils.classpathFileToObject;
import static ru.alfabank.skillbox.examples.moduletests.utils.TestUtils.classpathFileToString;
import static ru.alfabank.skillbox.examples.moduletests.utils.TestUtils.stringToObject;

@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PermitAllResourceServerWebSecurityConfig.class})
@AutoConfigureMockMvc
//WireMock (1)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("integTest-lesson4")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModuleTest_PostgresTestcontainer {

    @Container
    private static final PostgreSQLContainer<PostgresContainerWrapper> postgresContainer = new PostgresContainerWrapper();

    @DynamicPropertySource
    public static void initSystemParams(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void initStubs() {
        //WireMock (2)
        WireMock.reset();
        validationRestStub(HttpStatus.OK,
                "lesson4/responses/validate_OK.json");
    }

    @Test
    void applicationContextStartedSuccessfullyTest(ApplicationContext context) throws Exception {
        assertThat(context).isNotNull();
        // health-check
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")).andExpect(status().isOk());
    }

    @Test
    void saveSuccessfullyTest() throws Exception {
        JsonStateResponse expected = classpathFileToObject("/mvc-responses/lesson4/save_Ok.json", JsonStateResponse.class);
        // save
        mockMvc.perform(MockMvcRequestBuilders.post("/json/save")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(classpathFileToString("/mvc-requests/lesson4/save.json")))
                .andExpect(jsonPath("$.json", Matchers.equalTo(expected.getJson())))
                .andExpect(jsonPath("$.valid", Matchers.equalTo(expected.isValid())));
    }

    @Test
    void getSuccessfullyTest() throws Exception {
        //save
        String saveResponse = mockMvc.perform(MockMvcRequestBuilders.post("/json/save")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .content(classpathFileToString("/mvc-requests/lesson4/save.json")))
                .andReturn().getResponse()
                .getContentAsString();
        JsonStateResponse saveResponseString = stringToObject(saveResponse, JsonStateResponse.class);
        String id = saveResponseString.getId();

        JsonStateResponse expected = classpathFileToObject("/mvc-responses/lesson4/find_Ok.json", JsonStateResponse.class);
        // get previously saved
        mockMvc.perform(MockMvcRequestBuilders.get("/json/find")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .queryParam("id", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.json", Matchers.equalTo(expected.getJson())))
                .andExpect(jsonPath("$.id", Matchers.equalTo(id)))
                .andExpect(jsonPath("$.valid", Matchers.equalTo(expected.isValid())));
    }
}