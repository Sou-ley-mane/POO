package com.toolkit.crud.support;

import com.toolkit.crud.service.AbstractCrudService;

public class TestProductService extends AbstractCrudService<TestProduct, Long, TestProductDto, TestProductRepository> {

    public TestProductService(TestProductRepository repository, TestProductMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String entityName() {
        return "Produit";
    }
}
