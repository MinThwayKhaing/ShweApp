package com.app.shwe.controller;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class DemoController {

    @Autowired
    FileUploadService fileUploadService;
    // @GetMapping
    // public ResponseEntity<String> demo(){
    // return ResponseEntity.ok("Secure Api");
    // }

    @GetMapping
    public boolean testdeleteimage() {
        return fileUploadService.deleteFile("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJLZyBLZyIsImlhdCI6MTcyMzQ3OTkzMCwiZXhwIjoxNzIzNTY2MzMwfQ.Pz_mPppDVG9PI8ylBecqhu4q0tbmEj0W2EQM6Rs-hCw");
    }

}
