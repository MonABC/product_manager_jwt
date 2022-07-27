package com.example.product_manager.service.product;

import com.example.product_manager.model.entity.Product;
import com.example.product_manager.repo.IProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepo productRepo;

    @Override
    public Page<Product> getProductByCategoryId(Long id, Pageable pageable) {
        return productRepo.getProductByCategoryId(id, pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepo.findById(id);
    }

    @Override
    public Page<Product> findByName(String q, Pageable pageable) {
        if (Objects.equals(q, "")) {
            q = "%%";
        } else {
            q = "%" + q + "%";
        }
        return productRepo.searchProductByName(q, pageable);
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public void remove(long id) {
        productRepo.deleteById(id);
    }
}
