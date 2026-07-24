package com.toolkit.crud.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe de base pour toute entité JPA gérée par {@code common-crud} : identifiant généré,
 * et verrouillage optimiste via {@code @Version} pour détecter les mises à jour concurrentes
 * sans recourir à des verrous pessimistes coûteux.
 *
 * <p>Implémente {@link Persistable} (plutôt que de se fier à {@code id == null}) pour que
 * Spring Data traite correctement le cas d'un {@code save()} sur une entité déjà persistée
 * mais dont l'identifiant est assigné manuellement.
 *
 * <p>L'égalité est basée sur l'identifiant métier (comportement standard recommandé par
 * Spring Data / Hibernate pour les entités, cf. {@code AbstractPersistable}) : deux entités
 * transientes (id null) ne sont jamais égales entre elles.
 *
 * @param <ID> type de l'identifiant (doit être {@link Serializable})
 */
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> implements Persistable<ID> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @Version
    private Long version;

    @Override
    public ID getId() {
        return id;
    }

    protected void setId(ID id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BaseEntity<?> that)) {
            return false;
        }
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
