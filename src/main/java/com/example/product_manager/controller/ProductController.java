package com.example.product_manager.controller;

import com.example.product_manager.model.dto.CustomPage;
import com.example.product_manager.model.dto.ProductRequest;
import com.example.product_manager.model.entity.Product;
import com.example.product_manager.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/products")
public class ProductController {
    @Value("${file-upload}")
    String fileUpload;
    @Autowired
    private IProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<CustomPage> getProduct(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                                                 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                 @RequestParam(name = "size", required = false, defaultValue = "2") int size) {
        try {
            int pageCurrent = page >= 1 ? page - 1 : page;
            Pageable pageable = PageRequest.of(pageCurrent, size);
            Page<Product> productPage = productService.findByName(q, pageable);
            if (productPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<Product> products = productPage.getContent();
            Map<String, Object> paging = new HashMap<>();
            paging.put("currentPage", pageCurrent);
            paging.put("totalPages", productPage.getTotalPages());
            paging.put("toTalElement", productPage.getTotalElements());
            paging.put("size", size);
            CustomPage customPage = new CustomPage();
            customPage.setPageable(products);
            customPage.setPageable(paging);
            return new ResponseEntity<>(customPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductId(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        try {
            Optional<Product> optionalProduct = productService.findById(id);
            if (optionalProduct.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Product product = optionalProduct.get();
            MultipartFile multipartFile = productRequest.getImage();
            if (multipartFile == null && multipartFile.getSize() != 0) {
                File file = new File(fileUpload + product.getImage());
                if (file.exists()) {
                    file.delete();
                }
                String fileName = productRequest.getImage().getOriginalFilename();
                Long currentTime = System.currentTimeMillis();
                fileName = currentTime + fileName;
                product.setImage(fileName);
                try {
                    FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setDescription(productRequest.getDescription());
            product.setCategoryId(productRequest.getCategory());
            productService.save(product);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody ProductRequest productRequest) {
        String fileName = productRequest.getImage().getOriginalFilename();
        Long currentTime = System.currentTimeMillis();
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(productRequest.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productRequest.getId(), productRequest.getName(), productRequest.getPrice(), productRequest.getDescription(), fileName, productRequest.getCategory());
        return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
    }


}
