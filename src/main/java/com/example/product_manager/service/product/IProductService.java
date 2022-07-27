package com.example.product_manager.service.product;

import com.example.product_manager.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductService {
    Page<Product> getProductByCategoryId(Long id, Pageable pageable);

    Optional<Product> findById(Long id);

    Page<Product> findByName(String name, Pageable pageable);

    Product save(Product product);

    void remove(long id);
}
