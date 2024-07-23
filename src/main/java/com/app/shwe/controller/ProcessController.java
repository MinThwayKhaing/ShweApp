package com.app.shwe.controller;

import com.app.shwe.model.Process;
import com.app.shwe.service.FileUploadService;
import com.app.shwe.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping
    public List<Process> getAllProcesses() {
        return processService.getAllProcesses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Process> getProcessById(@PathVariable Long id) {
        Optional<Process> process = processService.getProcessById(id);
        return process.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Process addProcess(@RequestBody Process process, @RequestParam("file") MultipartFile file) {
        String imglink = fileUploadService.uploadFile(file);
        process.setImglink(imglink);
        return processService.addProcess(process);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Process> updateProcess(@PathVariable Long id, @RequestBody Process processDetails, @RequestParam("file") MultipartFile file) {
        String imglink = fileUploadService.uploadFile(file);
        processDetails.setImglink(imglink);
        return ResponseEntity.ok(processService.updateProcess(id, processDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Long id) {
        processService.deleteProcess(id);
        return ResponseEntity.noContent().build();
    }
}
