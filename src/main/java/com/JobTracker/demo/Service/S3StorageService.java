package com.JobTracker.demo.Service;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class S3StorageService {



    private final String bucketName;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3StorageService(
            @Value("${aws.s3.bucket-name:}") String bucketName,
            @Value("${aws.s3.region:}") String regionStr){

        this.bucketName = bucketName;

        Region region = (regionStr == null || regionStr.isBlank())
                ? Region.US_EAST_2
                : Region.of(regionStr);

        DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();

        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }


    public Map<String, List<Map<String, Object>>> listFiles(String prefix){
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .delimiter("/")
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listRequest);

        List<Map<String, Object>> folders = response.commonPrefixes().stream()
                .map(cp -> {
                    Map<String, Object> folder = new HashMap<>();
                    String fullPath = cp.prefix();
                    String[] parts = fullPath.split("/");
                    folder.put("name", parts[parts.length-1]);
                    folder.put("fullName", fullPath);
                    folder.put("isFolder", true);
                    return folder;
                }).toList();

        List<Map<String,Object>> files = response.contents().stream()
                .filter(obj -> !obj.key().equals(prefix))
                .map(obj -> {
                    Map<String, Object> file = new HashMap<>();
                    String key = obj.key();
                    String[] parts = key.split("/");
                    file.put("name", parts[parts.length-1]);
                    file.put("fullName", key);
                    file.put("isFolder", false);
                    return file;
                }).toList();

        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("folders", folders);
        result.put("files", files);
        return result;
    }


    public void uploadFile(String fileKey, MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public void  deleteFile(String fileKey){
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public String generateDownloadUrl(String fileKey){
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(b -> b.bucket(bucketName).key(fileKey))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    public void createFolder(String folderPath){

        String folderKey = folderPath.endsWith("/") ? folderPath : folderPath + "/";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(folderKey)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.empty());
    }


    @PreDestroy
    public void cleanUp(){
        if(s3Client != null) s3Client.close();
        if(s3Presigner != null) s3Presigner.close();
    }


}
