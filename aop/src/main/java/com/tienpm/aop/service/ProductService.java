package com.tienpm.aop.service;

import com.tienpm.aop.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    ProductEntity create(String name);
    List<ProductEntity> list();
}
