package ru.alfabank.skillbox.examples.moduletests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.client.WireMock;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alfabank.skillbox.examples.moduletests.config.PermitAllResourceServerWebSecurityConfig;
import ru.alfabank.skillbox.examples.moduletests.containers.PostgresContainerWrapper;
import ru.alfabank.skillbox.examples.moduletests.dto.JsonStateResponse;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alfabank.skillbox.examples.moduletests.utils.RestUtils.validationRestStub;
import static ru.alfabank.skillbox.examples.moduletests.utils.TestUtils.classpathFileToObject;
import static ru.alfabank.skillbox.examples.moduletests.utils.TestUtils.classpathFileToString;

@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("integTest-lesson5")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ModuleTest_MoreThenOneTestcontainer {

    @Container
    private static final PostgreSQLContainer<PostgresContainerWrapper> postgresContainer = new PostgresContainerWrapper();

    //KEYCLOAK (1)
    @Container
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer()
            .withRealmImportFile("realm-config.json")
            .withEnv("DB_VENDOR", "h2");

    @DynamicPropertySource
    public static void initSystemParams(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        //KEYCLOAK (2)
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/alfa-skillbox/protocol/openid-connect/certs");
    }

    @Autowired
    private MockMvc mockMvc;

    // KEYCLOAK (3)
    private final String authServerUrl = keycloakContainer.getAuthServerUrl() + "/realms/alfa-skillbox/protocol/openid-connect/token";

    @BeforeEach
    void initStubs() {
        WireMock.reset();
        validationRestStub(HttpStatus.OK,
                "lesson5/responses/validate_OK.json");
    }

    @Test
    void applicationContextStartedSuccessfullyTest(ApplicationContext context) throws Exception {
        assertThat(context).isNotNull();
        // health-check
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")).andExpect(status().isOk());
        getToken();
    }

    @Test
    void saveSuccessfullyTest() throws Exception {
        JsonStateResponse expected = classpathFileToObject("/mvc-responses/lesson5/save_Ok.json", JsonStateResponse.class);
        // save
        mockMvc.perform(MockMvcRequestBuilders.post("/json/save")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer " + getToken())
                .content(classpathFileToString("/mvc-requests/lesson5/save.json")))
                .andExpect(jsonPath("$.json", Matchers.equalTo(expected.getJson())))
                .andExpect(jsonPath("$.valid", Matchers.equalTo(expected.isValid())));
    }

    @Test
    void saveUnauthorizedTest() throws Exception {
        // when
        String fakeToken = "fake token";
        // save
        mockMvc.perform(MockMvcRequestBuilders.post("/json/save")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, "Bearer " + fakeToken)
                .content(classpathFileToString("/mvc-requests/lesson5/save.json")))
                .andExpect(status().isUnauthorized());
    }

    // KEYCLOAK (4)
    private String getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("grant_type", Collections.singletonList("client_credentials"));
        map.put("client_id", Collections.singletonList("modules-tests-client"));
        map.put("client_secret", Collections.singletonList("kFdfYNqWFPtb9UALCXNc4yhsS2zrX0XV"));
        KeyCloakToken token =
                restTemplate.postForObject(
                        authServerUrl, new HttpEntity<>(map, httpHeaders), KeyCloakToken.class);

        assertThat(token).isNotNull();
        return token.getAccessToken();
    }

    private static class KeyCloakToken {

        private final String accessToken;

        @JsonCreator
        KeyCloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }
    }
}
