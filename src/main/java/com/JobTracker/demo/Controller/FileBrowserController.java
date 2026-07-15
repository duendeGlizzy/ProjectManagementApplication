package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Service.S3StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileBrowserController {

    private final S3StorageService s3StorageService;

    public FileBrowserController(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    @GetMapping
    public ResponseEntity<?> getBucketFiles(@RequestParam(required = false, defaultValue = "") String prefix) {
        return ResponseEntity.ok(s3StorageService.listFiles(prefix));
    }

    @GetMapping("/download")
    public ResponseEntity<?> getDownloadLink(@RequestParam String key) {
        String url = s3StorageService.generateDownloadUrl(key);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "prefix", required = false, defaultValue = "") String prefix){
        try{
            String fileKey = prefix + file.getOriginalFilename();
            s3StorageService.uploadFile(fileKey, file);
            return ResponseEntity.ok(Map.of("message", "Successfully uploaded "));
        }catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam String key) {
        try{
            s3StorageService.deleteFile(key);
            return ResponseEntity.ok(Map.of("message", "Successfully deleted "));
        }catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }


}
