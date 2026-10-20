package com.Showroom.Microservice.ApiGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false", "eureka.client.enabled=false",
    "spring.datasource.url=jdbc:h2:mem:auth;MODE=MySQL;NON_KEYWORDS=USER",
    "spring.datasource.username=sa", "spring.datasource.password=", "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop", "jwt.secret=test-only-secret-with-at-least-32-bytes"
})
@AutoConfigureMockMvc
class AuthIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Test void registerLoginAndAuthenticateWithBearerToken() throws Exception {
        String credentials = "{\"username\":\"integration-user\",\"password\":\"password\"}";
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(credentials)).andExpect(status().isOk());
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(credentials)).andExpect(status().isConflict());
        String body = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(credentials))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String token = json.readTree(body).get("token").asText();
        mvc.perform(get("/protected-test-path")).andExpect(status().isUnauthorized());
        mvc.perform(get("/protected-test-path").header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
        mvc.perform(get("/protected-test-path").header("Authorization", "Bearer invalid")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"integration-user\",\"password\":\"wrong\"}")).andExpect(status().isUnauthorized());
    }
    @Test void blankCredentialsAreRejected() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"\",\"password\":\"\"}")).andExpect(status().isBadRequest());
    }
    @Test void unknownAuthPathReturnsNotFound() throws Exception {
        mvc.perform(post("/api/auth/unknown").contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"user\",\"password\":\"password\"}"))
            .andExpect(status().isNotFound());
    }

    @Test void errorDispatchDoesNotRequireLogin() throws Exception {
        mvc.perform(get("/error").with(request -> {
            request.setDispatcherType(jakarta.servlet.DispatcherType.ERROR);
            request.setAttribute(jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE, 404);
            return request;
        })).andExpect(status().isNotFound());
    }
}
