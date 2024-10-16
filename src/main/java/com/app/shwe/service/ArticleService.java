package com.app.shwe.service;

import com.app.shwe.dto.ArticleDTO;
import com.app.shwe.dto.ArticleRequest;
import com.app.shwe.model.Activity;
import com.app.shwe.model.Article;
import com.app.shwe.repository.ActivityRepository;
import com.app.shwe.repository.ArticleRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.FilesSerializationUtil;
import com.app.shwe.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FileUploadService fileUploadService;
    
    @Autowired
	private UserRepository userRepository;

    public ResponseEntity<String> saveArticle(List<MultipartFile> images, ArticleRequest articleRequest) {
        try {
            Article article = new Article();
            article.setTitle(articleRequest.getTitle());
            article.setDescription(articleRequest.getDescription());

            Optional<Activity> activityOptional = activityRepository.findById(articleRequest.getActivity_id());
            if (!activityOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid activity ID: " + articleRequest.getActivity_id());
            }

            Activity activity = activityOptional.get();
            article.setActivity(activity);
       
            List<String> imageUrl = fileUploadService.uploadFiles(images);
            String serializedImages = FilesSerializationUtil.serializeImages(imageUrl);
            article.setImageUrl(serializedImages);
            article.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
            articleRepository.save(article);
            return ResponseEntity.status(HttpStatus.OK).body("Article saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving Article: " + e.getMessage());
        }
    }

    public Optional<Article> getArticleById(int id) {
        return articleRepository.findById(id);
    }

    public Page<ArticleDTO> getAllArticles(int page, int size) {
            Pageable pageable = PageRequest.of(page, size);
            return articleRepository.findDTOByArticleId(pageable);
        
    }
    
    // public ResponseEntity<?> getAllArticles(int page, int size) {
    // try {
    // Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
    // Page<ArticleDTO> articles = articleRepository.findDTOByArticleId(pageable);
    // return ResponseEntity.status(HttpStatus.OK).body(articles);

    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body("Error occurred while selecting Activities: " + e.getMessage());
    // }
    // }

    // public ResponseEntity<?> getArticlesByActivityId(int activityId, int page,
    // int size) {
    // try {
    // Pageable pageable = PageRequest.of(page, size);
    // Page<ArticleDTO> articlesPage =
    // articleRepository.findDTOByActivityId(activityId, pageable);
    // if (articlesPage.isEmpty()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No articles found
    // for the given activity ID.");
    // }
    // return ResponseEntity.status(HttpStatus.OK).body(articlesPage);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body("Error occurred while retrieving articles: " + e.getMessage());
    // }
    // }

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

    public List<Article> getAllArticlesList() {
        List<Article> articleList = articleRepository.findAll();
        return articleList;
    }
}
