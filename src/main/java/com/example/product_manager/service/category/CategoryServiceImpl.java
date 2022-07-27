package com.example.product_manager.service.category;

import com.example.product_manager.model.entity.Category;
import com.example.product_manager.repo.ICategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryRepo categoryRepo;

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepo.findById(id);
    }

    @Override
    public Page<Category> findByName(String q, Pageable pageable) {
        if (Objects.equals(q, "")) {
            q = "%%";
        } else {
            q = "%" + q + "%";
        }
        return categoryRepo.searchCategory(q, pageable);

    }

    @Override
    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public void remove(Long id) {
        categoryRepo.deleteCategoryById(id);
    }
}
