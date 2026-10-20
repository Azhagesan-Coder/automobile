package com.Showroom.Config_Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasItem;
@SpringBootTest
@AutoConfigureMockMvc
class ConfigServerConfigurationTest {
    @Autowired MockMvc mvc;
    @Test void servesBundledCustomerConfiguration() throws Exception {
        mvc.perform(get("/customer-management/default")).andExpect(status().isOk())
            .andExpect(jsonPath("$.propertySources[*].source['server.port']", hasItem(8081)));
    }
}
