package com.example.product_manager.controller;

import com.example.product_manager.model.dto.CategoryRequest;
import com.example.product_manager.model.dto.CustomPage;
import com.example.product_manager.model.entity.Category;
import com.example.product_manager.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<CustomPage> getCategories(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                                                    @RequestParam(name = "page", required = false, defaultValue = "") int page,
                                                    @RequestParam(name = "size", required = false, defaultValue = "") int size) {
        int pageCurrent = page >= 1 ? page - 1 : page;
        Pageable pageable = PageRequest.of(pageCurrent, size);
        Page<Category> categoryPage = categoryService.findByName(q, pageable);
        if (categoryPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Category> categories = categoryPage.getContent();
        Map<String, Object> paging = new HashMap<>();
        paging.put("currentPage", pageCurrent);
        paging.put("size", size);
        paging.put("toTalPage", categoryPage.getTotalPages());
        paging.put("toTalElement", categoryPage.getTotalElements());
        CustomPage customPage = new CustomPage();
        customPage.setContent(categories);
        customPage.setPageable(paging);
        return new ResponseEntity<>(customPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        return categoryOptional.map(category -> new ResponseEntity<>(category, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        if (!categoryOptional.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(categoryOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            Category category = new Category(categoryRequest.getId(), categoryRequest.getName());
            return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        try {
            Optional<Category> optionalCategory = categoryService.findById(id);
            if (!optionalCategory.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Category category = new Category(categoryRequest.getId(),categoryRequest.getName());
            return new ResponseEntity<>(categoryService.save(category), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
