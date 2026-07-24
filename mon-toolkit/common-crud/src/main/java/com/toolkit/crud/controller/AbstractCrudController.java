package com.toolkit.crud.controller;

import com.toolkit.crud.service.CrudOperations;
import com.toolkit.pagination.dto.PagedResponse;
import com.toolkit.pagination.util.PageRequestUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 * Contrôleur REST CRUD générique. La sous-classe n'a qu'à porter les annotations
 * {@code @RestController} / {@code @RequestMapping} et fournir le service concret :
 *
 * <pre>{@code
 * @RestController
 * @RequestMapping("/api/users")
 * public class UserController extends AbstractCrudController<Long, UserDto> {
 *     private final UserService userService;
 *
 *     public UserController(UserService userService) {
 *         this.userService = userService;
 *     }
 *
 *     @Override
 *     protected CrudOperations<Long, UserDto> service() {
 *         return userService;
 *     }
 * }
 * }</pre>
 *
 * <p>Les paramètres de pagination ({@code page}, {@code size}, {@code sort}) sont résolus
 * via {@link PageRequestUtils} avec les valeurs par défaut du toolkit.
 *
 * @param <ID>  type de l'identifiant de la ressource, tel qu'utilisé dans l'URL
 * @param <DTO> type du DTO exposé par l'API
 */
public abstract class AbstractCrudController<ID extends Serializable, DTO> {

    /**
     * Fournit le service métier concret. Implémenté par la sous-classe, généralement
     * en retournant simplement le service injecté par constructeur.
     */
    protected abstract CrudOperations<ID, DTO> service();

    @PostMapping
    public ResponseEntity<DTO> create(@Valid @RequestBody DTO dto) {
        DTO created = service().create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTO> getById(@PathVariable ID id) {
        return ResponseEntity.ok(service().getById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<DTO>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) List<String> sort) {
        Pageable pageable = PageRequestUtils.toPageable(page, size, sort);
        return ResponseEntity.ok(service().list(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@PathVariable ID id, @Valid @RequestBody DTO dto) {
        return ResponseEntity.ok(service().update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service().delete(id);
        return ResponseEntity.noContent().build();
    }
}
