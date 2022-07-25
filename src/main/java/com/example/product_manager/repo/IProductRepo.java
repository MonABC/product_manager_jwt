package com.example.product_manager.repo;

import com.example.product_manager.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepo extends JpaRepository<Product, Long> {
    @Query(value = "select * from Product p join Category c on p.categoryId = c.id where p.categoryId = :id", nativeQuery = true)
    Page<Product> getProductByCategoryId(@Param("id") long id, Pageable pageable);

    @Query(value = "select * from Product p join Category c on p.categoryId = c.id where c.isDelete =false and p.name like :q", nativeQuery = true)
    Page<Product> searchProductByName(@Param("q") String q, Pageable pageable);
}
