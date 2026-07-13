package com.JobTracker.demo.Service;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
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

    @PreDestroy
    public void cleanUp(){
        if(s3Client != null) s3Client.close();
        if(s3Presigner != null) s3Presigner.close();
    }


}
