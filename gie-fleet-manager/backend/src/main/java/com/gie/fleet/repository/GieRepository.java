package com.gie.fleet.repository;

import com.gie.fleet.entity.Gie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GieRepository extends JpaRepository<Gie, Long> {
}
