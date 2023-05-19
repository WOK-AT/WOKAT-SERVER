package com.sopt.wokat.infra.aws;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sopt.wokat.domain.place.dto.PlaceImageUploadDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3PlaceUploader {
    
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.buckets.bucket2}")
    public String bucket;

    public PlaceImageUploadDTO uploadS3ProfileImage(String classId, MultipartFile file) throws IOException {
        String fileName = generateUniqueFileName();
        String filePath = getDirectoryName(classId) + fileName;

        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        String s3URL = amazonS3Client.getUrl(bucket, filePath).toString();
        PlaceImageUploadDTO imageUploadDTO = new PlaceImageUploadDTO(s3URL, fileName);
        return imageUploadDTO;
    }

    //! 객체 삭제
	public void deleteObject(String classId, String storedFileName) throws AmazonServiceException {
        String filePath = getDirectoryName(classId);
		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath + storedFileName));
	}

    //! 버킷 내부 폴더 경로 설정 
    private String getDirectoryName(String classId) {
        String filePath = "";

        switch(classId) {
            case "0": filePath = "freeZone/";
            case "1": filePath = "meetingRoom/";
            case "2": filePath = "cafe/";
        }

        return filePath;
    }

    private String generateUniqueFileName() {
        return UUID.randomUUID().toString();
    }

}
