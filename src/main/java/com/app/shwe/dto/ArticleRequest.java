package com.app.shwe.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.model.Activity;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ArticleRequest {

    private String title;
    private String description;
    private MultipartFile imageFile;
    // private String categoryTag;
    private int activity_id;

    
}
