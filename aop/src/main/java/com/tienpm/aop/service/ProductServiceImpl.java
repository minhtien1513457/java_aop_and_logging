package com.tienpm.aop.service;

import com.tienpm.aop.annotations.LogOperationLayer;
import com.tienpm.aop.entity.ProductEntity;
import com.tienpm.aop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Override
    @LogOperationLayer
    public ProductEntity create(String name) {
        ProductEntity entity = ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .build();
        return productRepository.save(entity);
    }

    @Override
    @LogOperationLayer
    public List<ProductEntity> list() {
        return productRepository.findAll();
    }
}
