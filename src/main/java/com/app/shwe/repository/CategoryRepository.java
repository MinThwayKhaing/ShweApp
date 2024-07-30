package com.app.shwe.repository;

import com.app.shwe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT * FROM category", nativeQuery = true)
    List<Category> findAllCategories();

    @Query(value = "SELECT * FROM category WHERE id = :id", nativeQuery = true)
    Optional<Category> findCategoryById(@Param("id") int id);

    @Modifying
    @Query(value = "INSERT INTO category (name, label) VALUES (:name, :label)", nativeQuery = true)
    void insertCategory(@Param("name") String name, @Param("label") String label);

    @Modifying
    @Query(value = "UPDATE category SET name = :name, label = :label WHERE id = :id", nativeQuery = true)
    void updateCategory(@Param("id") int id, @Param("name") String name, @Param("label") String label);

    @Modifying
    @Query(value = "DELETE FROM category WHERE id = :id", nativeQuery = true)
    void deleteCategoryById(@Param("id") int id);
}
