# common-pagination

Réponse paginée générique, résolution des paramètres `page`/`size`/`sort` et filtrage
dynamique via une `Specification` JPA générique.

## Installation

```xml
<dependency>
    <groupId>com.toolkit</groupId>
    <artifactId>common-pagination</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Utilisation

### 1. Résoudre la pagination depuis les query params

```java
@GetMapping("/users")
public PagedResponse<UserDto> list(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) List<String> sort) {

    Pageable pageable = PageRequestUtils.toPageable(page, size, sort);
    Page<User> users = userRepository.findAll(pageable);
    return PagedResponse.from(users, userMapper::toDto);
}
```

Appel : `GET /users?page=0&size=10&sort=lastName,asc&sort=createdAt,desc`

### 2. Filtrage dynamique avec GenericSpecificationBuilder

```java
Specification<User> spec = new GenericSpecificationBuilder<User>()
        .with("status", SearchOperation.EQUALS, "ACTIVE")
        .with("createdAt", SearchOperation.GREATER_THAN_OR_EQUAL, since)
        .with("address.city", SearchOperation.EQUALS, "Paris")
        .build();

Page<User> result = userRepository.findAll(spec, pageable);
```

`GenericSpecificationBuilder.build()` renvoie `null` si aucun critère n'est fourni, ce qui
correspond à la convention Spring Data pour "aucun filtre" (`Specification.where(null)`).

### Opérateurs disponibles (`SearchOperation`)

`EQUALS`, `NOT_EQUALS`, `GREATER_THAN`, `GREATER_THAN_OR_EQUAL`, `LESS_THAN`,
`LESS_THAN_OR_EQUAL`, `LIKE` (insensible à la casse, `%contains%`), `IN` (attend une
`Collection`), `IS_NULL`, `IS_NOT_NULL`.

Les champs imbriqués sont supportés via la notation pointée (`"address.city"`).

## Réponse JSON produite

```json
{
  "data": [ { "id": 1, "email": "a@b.com" } ],
  "page": 0,
  "size": 10,
  "totalElements": 42,
  "totalPages": 5
}
```
