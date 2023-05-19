package com.sopt.wokat.infra.aws;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sopt.wokat.domain.place.exception.FileUploadException;
import com.sopt.wokat.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3PlaceUploader {
    
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.buckets.bucket2}")
    public String bucket;

    public List<String> uploadS3ProfileImage(String classId, List<MultipartFile> multipartFile) throws IOException {
        List<String> resultList = new ArrayList<>();

        multipartFile.forEach(file -> {
            String fileName = generateFileName(file.getOriginalFilename());
            String filePath = getDirectoryName(classId) + fileName;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(
                    bucket, filePath, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new FileUploadException();
            }

            String s3URL = amazonS3Client.getUrl(bucket, filePath).toString();
            resultList.add(s3URL);
        });

        return resultList;
    }

    //! 객체 삭제
	public void deleteObject(String classId, String s3URL) throws AmazonServiceException {
        String filePath = getDirectoryName(classId);
        String fileName = s3URL.split(filePath)[1];

		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath + fileName));
	}

    //! 버킷 내부 폴더 경로 설정 
    private String getDirectoryName(String classId) {
        String filePath = "";

        switch(classId) {
            case "0": 
                filePath = "freeZone/";
                break;
            case "1": 
                filePath = "meetingRoom/";
                break;
            case "2": 
                filePath = "cafe/";
                break;
        }

        return filePath;
    }

    private String generateFileName(String fileName) { 
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new FileUploadException(ErrorCode.INVALID_FILE);
        }
    }

}
