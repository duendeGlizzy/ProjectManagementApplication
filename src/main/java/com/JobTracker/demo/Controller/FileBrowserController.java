package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Service.S3StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileBrowserController {

    private final S3StorageService s3StorageService;

    public FileBrowserController(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    @GetMapping
    public ResponseEntity<?> getBucketFiles() {
        return ResponseEntity.ok(s3StorageService.listFiles());
    }

    @GetMapping("/download")
    public ResponseEntity<?> getDownloadLink(@RequestParam String key) {
        String url = s3StorageService.generateDownloadUrl(key);
        return ResponseEntity.ok(Map.of("url", url));
    }


}
