package com.gie.fleet.repository;

import com.gie.fleet.entity.TypeFrais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TypeFraisRepository extends JpaRepository<TypeFrais, Long> {

    List<TypeFrais> findByGieId(Long gieId);

    List<TypeFrais> findByGieIdAndActifTrue(Long gieId);

    Optional<TypeFrais> findByIdAndGieId(Long id, Long gieId);
}
