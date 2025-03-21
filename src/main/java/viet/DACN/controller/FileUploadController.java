package viet.DACN.controller;

import lombok.extern.slf4j.Slf4j;
import viet.DACN.service.MinioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final MinioService minioService;

    public FileUploadController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        log.info("Received request with file: {}", file != null ? file.getOriginalFilename() : "null");
        try {
            String url = minioService.uploadAndGetUrl(file);
            log.info("File uploaded successfully, URL: {}", url);
            return ResponseEntity.ok(url);
        } catch (IllegalArgumentException e) {
            log.error("Invalid file: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid file: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("Upload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }
}