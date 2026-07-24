package com.toolkit.crud.support;

import com.toolkit.crud.mapper.CrudMapper;

public class TestProductMapper implements CrudMapper<TestProduct, TestProductDto> {

    @Override
    public TestProductDto toDto(TestProduct entity) {
        return new TestProductDto(entity.getId(), entity.getName(), entity.getCategory(), entity.getVersion());
    }

    @Override
    public TestProduct toEntity(TestProductDto dto) {
        return new TestProduct(dto.name(), dto.category());
    }

    @Override
    public void updateEntityFromDto(TestProductDto dto, TestProduct entity) {
        entity.setName(dto.name());
        entity.setCategory(dto.category());
    }
}
