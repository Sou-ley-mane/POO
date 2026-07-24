package com.toolkit.crud.controller;

import com.toolkit.crud.service.CrudOperations;
import com.toolkit.crud.support.TestProductDto;
import com.toolkit.exceptions.core.NotFoundException;
import com.toolkit.exceptions.handler.GlobalExceptionHandler;
import com.toolkit.pagination.dto.PagedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Vérifie, avec un {@link CrudOperations} simulé, que le contrôleur générique expose les
 * bonnes routes HTTP, convertit correctement l'identifiant générique ({@code Long} ici) et
 * délègue au {@code GlobalExceptionHandler} de {@code common-exceptions} pour les erreurs.
 *
 * <p>La résolution du type générique {@code ID} sur {@code @PathVariable} repose sur le fait
 * que le contrôleur concret déclare directement des types concrets
 * ({@code AbstractCrudController<Long, TestProductDto>}) : Spring MVC résout alors le type
 * réel via le bean cible, comme documenté pour les contrôleurs abstraits génériques.
 */
class AbstractCrudControllerTest {

    @SuppressWarnings("unchecked")
    private final CrudOperations<Long, TestProductDto> service = mock(CrudOperations.class);
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestProductController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        when(service.create(any())).thenReturn(new TestProductDto(1L, "Clavier", "Informatique", 0L));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Clavier\",\"category\":\"Informatique\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clavier"));
    }

    @Test
    void shouldGetProductByLongId() throws Exception {
        when(service.getById(42L)).thenReturn(new TestProductDto(42L, "Souris", "Informatique", 0L));

        mockMvc.perform(get("/api/products/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42));
    }

    @Test
    void shouldReturn404WhenResourceIsMissing() throws Exception {
        when(service.getById(999L)).thenThrow(NotFoundException.forResource("Produit", 999L));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ERR-NOTFOUND-404"));
    }

    @Test
    void shouldListWithDefaultPagination() throws Exception {
        when(service.list(any(Pageable.class))).thenReturn(new PagedResponse<>(List.of(), 0, 20, 0, 0));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(20));
    }

    @Test
    void shouldDeleteProductAndReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @RestController
    @RequestMapping("/api/products")
    static class TestProductController extends AbstractCrudController<Long, TestProductDto> {
        private final CrudOperations<Long, TestProductDto> service;

        TestProductController(CrudOperations<Long, TestProductDto> service) {
            this.service = service;
        }

        @Override
        protected CrudOperations<Long, TestProductDto> service() {
            return service;
        }
    }
}
