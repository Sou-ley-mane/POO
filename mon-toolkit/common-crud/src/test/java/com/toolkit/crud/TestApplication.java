package com.toolkit.crud;

import com.toolkit.crud.support.TestProduct;
import com.toolkit.crud.support.TestProductRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration Spring Boot minimale requise par {@code @DataJpaTest} : ce module est une
 * librairie et ne fournit pas de classe {@code @SpringBootApplication} de production, cette
 * classe de test sert uniquement de point d'ancrage pour le contexte des tests d'intégration JPA.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = TestProduct.class)
@EnableJpaRepositories(basePackageClasses = TestProductRepository.class)
public class TestApplication {
}
