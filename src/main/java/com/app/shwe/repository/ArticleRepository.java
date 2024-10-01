package com.app.shwe.repository;

import com.app.shwe.dto.ArticleDTO;
import com.app.shwe.model.Article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
        @Query("SELECT new com.app.shwe.dto.ArticleDTO(a.id, a.title, a.description, a.imageUrl, a.createdDate, a.updatedDate, a.createdBy, a.updatedBy, a.period) "
                        +
                        "FROM Article a WHERE a.activity.id = :activityId")
        Page<ArticleDTO> findDTOByActivityId(@Param("activityId") int activityId, Pageable pageable);

        // @Query("SELECT new com.app.shwe.dto.ArticleDTO(a.id, a.title, a.description, a.imageUrl, a.createdDate, a.updatedDate, a.createdBy, a.updatedBy, a.period, act.name) "
        //                 + "FROM Article a JOIN a.activity act")
        // Page<ArticleDTO> findDTOByArticleId(Pageable pageable);

}
