package com.toolkit.exceptions.handler;

import com.toolkit.exceptions.core.ConflictException;
import com.toolkit.exceptions.core.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnProblemDetailWithNotFoundStatus() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errorCode").value("ERR-NOTFOUND-404"))
                .andExpect(jsonPath("$.detail").value("Utilisateur '42' introuvable"))
                .andExpect(jsonPath("$.resourceId").value("42"));
    }

    @Test
    void shouldReturnProblemDetailWithConflictStatus() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("ERR-CONFLICT-409"));
    }

    @Test
    void shouldReturnGenericInternalErrorWithoutLeakingDetails() throws Exception {
        mockMvc.perform(get("/test/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("ERR-INTERNAL-500"))
                .andExpect(jsonPath("$.detail").value("Une erreur interne est survenue. Veuillez réessayer ultérieurement."));
    }

    @RestController
    static class TestController {

        @GetMapping("/test/not-found")
        void notFound() {
            throw NotFoundException.forResource("Utilisateur", 42);
        }

        @GetMapping("/test/conflict")
        void conflict() {
            throw new ConflictException("conflit");
        }

        @GetMapping("/test/boom")
        void boom() {
            throw new IllegalStateException("secret interne à ne pas divulguer");
        }
    }
}
