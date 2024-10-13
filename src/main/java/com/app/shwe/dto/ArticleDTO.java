package com.app.shwe.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO implements Serializable {

    private int id;
    private String title;
    private String name;
    private String description;
    private String imageUrl;
    private Date createdDate;
    private Date updatedDate;
    private int createdBy;
    private int updatedBy;
    private String period;
}
