package com.app.shwe.repository;

import com.app.shwe.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    // JpaRepository provides CRUD operations and pagination methods
}
