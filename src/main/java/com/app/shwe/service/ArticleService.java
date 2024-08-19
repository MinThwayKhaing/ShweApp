package com.app.shwe.service;

import com.app.shwe.dto.ArticleRequest;
import com.app.shwe.model.Activity;
import com.app.shwe.model.Article;
import com.app.shwe.repository.ActivityRepository;
import com.app.shwe.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FileUploadService fileUploadService;

    public ResponseEntity<String> saveArticle(ArticleRequest article) {
        try {
            Article act = new Article();
            act.setTitle(article.getTitle());
            act.setDescription(article.getDescription());

            Optional<Activity> activityOptional = activityRepository.findById(article.getActivity_id());
            String imageUrl = fileUploadService.uploadFile(article.getImageFile());
            act.setImageUrl(imageUrl);
            if (!activityOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid activity ID: " + article.getActivity_id());
            }
            article.setActivity(activityOptional.get());

            articleRepository.save(act);
            return ResponseEntity.status(HttpStatus.OK).body("Article save  successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving Article: " + e.getMessage());
        }
    }

    public Optional<Article> getArticleById(int id) {
        return articleRepository.findById(id);
    }

    public Article updateArticle(int id, Article updatedArticle) {
        return articleRepository.findById(id).map(article -> {
            article.setTitle(updatedArticle.getTitle());
            article.setDescription(updatedArticle.getDescription());
            article.setImageUrl(updatedArticle.getImageUrl());
            article.setActivity(updatedArticle.getActivity());
            return articleRepository.save(article);
        }).orElseThrow(() -> new RuntimeException("Article not found with id " + id));
    }

    public ResponseEntity<String> deleteArticle(int id) {

        Optional<Article> activityOptional = articleRepository.findById(id);
        Article act = activityOptional.get();
        boolean status = fileUploadService.deleteFile(act.getImageUrl());
        if (!status) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured when deleting s3 link");
        }
        articleRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Deleted successfully");

    }

    public Page<Article> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAll(pageable);
    }
}
