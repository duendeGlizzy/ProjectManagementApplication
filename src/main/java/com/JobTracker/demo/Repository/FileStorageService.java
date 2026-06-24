package com.JobTracker.demo.Repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String storeFile(MultipartFile file, String folder);
    Resource loadFileAsResource(String fileKey);
    void deleteFile(String fileKey);

}
