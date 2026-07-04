package com.JobTracker.demo.Service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3StorageService {

    private final String bucketName = "bucketName";

    private final S3Client s3Client = S3Client.create();
    private final S3Presigner s3Presigner = S3Presigner.create();


    public List<String> listFiles(){
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        return s3Client.listObjectsV2(listRequest).contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    public String generateDownloadUrl(String fileKey){
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(b -> b.bucket(bucketName).key(fileKey))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }


}
