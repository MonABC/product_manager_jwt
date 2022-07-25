package com.example.product_manager.repo;

import com.example.product_manager.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepo extends JpaRepository<Category, Long> {
    @Query(value = "select * from Category where isDelete =false", nativeQuery = true)
    Page<Category> findAllCategory(Pageable pageable);


    @Query(value = "select * from Category c where c.name like :q and c.isDelete = false", nativeQuery = true)
    Page<Category> searchCategory(@Param("q") String q, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Category set isDelete = true  where id = ?1", nativeQuery = true)
    void deleteCategoryById(Long id);
}
