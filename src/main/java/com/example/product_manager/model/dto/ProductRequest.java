package com.example.product_manager.model.dto;

import com.example.product_manager.model.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;
    private String name;
    private double price;
    private String description;
    private MultipartFile image;
    private Category category;

    public ProductRequest(String name, double price, String description, MultipartFile image, Category category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.category = category;
    }
}
