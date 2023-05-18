package com.sopt.wokat.infra.aws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sopt.wokat.domain.member.dto.ProfileImageUploadDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DownlaodFileS3Uploader {
    
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.buckets.bucket1}")
    public String bucket;

    public ProfileImageUploadDTO uploadS3ProfileImage(String imageURL) throws IOException {
        byte[] imageData = downloadImageFromURL(imageURL);

        String fileName = generateUniqueFileName();
        String filePath = "kakao/" + fileName;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentDisposition("inline");
        metadata.setContentLength(imageData.length);

        amazonS3Client.putObject(bucket, filePath, inputStream, metadata);

        String s3URL = amazonS3Client.getUrl(bucket, filePath).toString();
        ProfileImageUploadDTO imageUploadDTO = new ProfileImageUploadDTO(s3URL, fileName); 
        return imageUploadDTO;
    }

    //! 객체 삭제
	public void deleteObject(String storedFileName) throws AmazonServiceException {
		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, "kakao/" + storedFileName));
	}

    private byte[] downloadImageFromURL(String imageURL) throws IOException {
        URL url = new URL(imageURL);
        return StreamUtils.copyToByteArray(url.openStream());
    }
    
    private String generateUniqueFileName() {
        return UUID.randomUUID().toString();
    }
}
