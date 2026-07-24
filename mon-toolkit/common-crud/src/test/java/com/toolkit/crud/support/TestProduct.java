package com.toolkit.crud.support;

import com.toolkit.crud.model.BaseEntity;
import jakarta.persistence.Entity;

@Entity
public class TestProduct extends BaseEntity<Long> {

    private String name;
    private String category;

    protected TestProduct() {
    }

    public TestProduct(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
