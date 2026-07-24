package com.toolkit.crud.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestProductRepository extends JpaRepository<TestProduct, Long>, JpaSpecificationExecutor<TestProduct> {
}
