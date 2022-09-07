package ru.alfabank.skillbox.examples.moduletests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.alfabank.skillbox.examples.moduletests.dto.JsonStateResponse;
import ru.alfabank.skillbox.examples.moduletests.services.JsonService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// MOCKMVC (1)
@WebMvcTest
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("integTest-lesson2")
public class ModuleTest_WebMvc {

    // MOCKMVC (2)
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonService jsonService;

    @Test
    void applicationContextStartedSuccessfullyTest(ApplicationContext context) throws Exception {
        when(jsonService.validateAndSave(any())).thenReturn(JsonStateResponse.builder().build());
        assertThat(context).isNotNull();
        // health-check
        mockMvc.perform(MockMvcRequestBuilders.get("/json/find"))
                .andExpect(status().isUnauthorized());
    }
}
