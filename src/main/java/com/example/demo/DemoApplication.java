package com.example.demo;

import java.net.URL;
import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@SpringBootApplication
public class DemoApplication {
    private static final String BUCKET_NAME = "techcourse-project-2024";
    private static final String KEY_NAME = "pengcook/frog.png";
    private static Region region = Region.AP_NORTHEAST_2;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        URL putPresignedUrl = generatePresignedPutUrl(presigner, BUCKET_NAME, KEY_NAME);
        System.out.println("Presigned PUT URL: " + putPresignedUrl);

        presigner.close();
    }


    private static URL generatePresignedPutUrl(S3Presigner presigner, String bucketName, String keyName) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        return presigner.presignPutObject(presignRequest).url();
    }
}
