// src/main/java/com/app/shwe/service/FileUploadService.java
package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileUploadService {

    private final S3Client s3Client;

    @Value("${cloud.do.bucket.name}")
    private String bucketName;

    public FileUploadService(@Value("${cloud.do.credentials.accessKey}") String accessKey,
                             @Value("${cloud.do.credentials.secretKey}") String secretKey,
                             @Value("${cloud.do.endpoint}") String endpoint) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of("sgp1"))
                .endpointOverride(URI.create("https://" + endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
    
    public List<String> uploadFiles(List<MultipartFile> files) {
        return files.stream()
                    .map(this::uploadFile)
                    .collect(Collectors.toList());
    }

    public String uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File convertedFile = convertMultiPartToFile(file);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl("public-read")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(convertedFile));
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
    }

    private File convertMultiPartToFile(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error converting file", e);
        }
        return convFile;
    }
}
