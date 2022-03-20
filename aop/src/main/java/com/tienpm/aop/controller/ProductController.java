package com.tienpm.aop.controller;

import com.tienpm.aop.annotations.LogControllerLayer;
import com.tienpm.aop.entity.ProductEntity;
import com.tienpm.aop.request.external.CreateProductRequest;
import com.tienpm.aop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    @LogControllerLayer
    public ProductEntity create(@RequestBody CreateProductRequest req) {
        return service.create(req.getName());
    }

    @GetMapping
    @LogControllerLayer
    public List<ProductEntity> list() {
        return service.list();
    }

}
