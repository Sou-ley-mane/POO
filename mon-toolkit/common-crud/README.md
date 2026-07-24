# common-crud

Service et contrôleur CRUD génériques : élimine le code répétitif de create/read/update/delete
et de liste paginée pour toute ressource REST classique adossée à Spring Data JPA.

## Installation

```xml
<dependency>
    <groupId>com.toolkit</groupId>
    <artifactId>common-crud</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Utilisation (< 5 minutes)

### 1. Entité : étendre `BaseEntity<ID>`

```java
@Entity
public class User extends BaseEntity<Long> {
    private String email;
    private String fullName;
    // getters/setters
}
```

`BaseEntity` fournit l'identifiant généré et le champ `@Version` pour le contrôle optimiste.

### 2. Repository : ajouter `JpaSpecificationExecutor`

```java
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
```

### 3. Mapper : implémenter `CrudMapper<Entity, DTO>`

```java
@Component
public class UserMapper implements CrudMapper<User, UserDto> {
    public UserDto toDto(User entity) { return new UserDto(entity.getId(), entity.getEmail(), entity.getFullName()); }
    public User toEntity(UserDto dto) { User u = new User(); u.setEmail(dto.email()); u.setFullName(dto.fullName()); return u; }
    public void updateEntityFromDto(UserDto dto, User entity) { entity.setEmail(dto.email()); entity.setFullName(dto.fullName()); }
}
```

(Peut aussi être généré par MapStruct : faire implémenter `CrudMapper<User, UserDto>` à
votre interface `@Mapper(componentModel = "spring")`.)

### 4. Service : étendre `AbstractCrudService`

```java
@Service
public class UserService extends AbstractCrudService<User, Long, UserDto, UserRepository> {
    public UserService(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String entityName() {
        return "Utilisateur";
    }
}
```

### 5. Contrôleur : étendre `AbstractCrudController`

```java
@RestController
@RequestMapping("/api/users")
public class UserController extends AbstractCrudController<Long, UserDto> {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected CrudOperations<Long, UserDto> service() {
        return userService;
    }
}
```

C'est terminé : `UserController` expose déjà `POST /api/users`, `GET /api/users/{id}`,
`GET /api/users` (paginé), `PUT /api/users/{id}` et `DELETE /api/users/{id}`.

## Filtrage dynamique

`AbstractCrudService` expose une surcharge `list(Specification<T>, Pageable)` pour combiner
avec `GenericSpecificationBuilder` du module `common-pagination` :

```java
Specification<User> spec = new GenericSpecificationBuilder<User>()
        .with("email", SearchOperation.LIKE, "@acme.com")
        .build();
PagedResponse<UserDto> results = userService.list(spec, pageable);
```

## Gestion des conflits optimistes

Une mise à jour concurrente sur une entité déjà modifiée (version `@Version` obsolète)
lève automatiquement un `OptimisticLockingFailureException` Spring Data, traduit par
`AbstractCrudService.update(...)` en `ConflictException` (HTTP 409), lui-même formaté en
RFC 7807 par `common-exceptions`.

## Erreurs

`getById`/`update`/`delete` lèvent `NotFoundException` (HTTP 404) si l'identifiant est
inconnu. Combiné avec `common-exceptions`, aucune gestion d'erreur supplémentaire n'est
nécessaire côté contrôleur.

## Limite connue

Le type générique `ID` sur `@PathVariable` n'est correctement résolu par Spring MVC que si
le contrôleur concret déclare directement des types concrets, ex :
`class UserController extends AbstractCrudController<Long, UserDto>`. Ne pas introduire de
niveau d'abstraction générique supplémentaire entre `AbstractCrudController` et le
contrôleur final.
