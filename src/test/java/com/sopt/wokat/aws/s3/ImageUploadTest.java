package com.sopt.wokat.aws.s3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sopt.wokat.infra.aws.S3ProfileImageUploader;

@SpringBootTest
public class ImageUploadTest {
    
    @Autowired
    private S3ProfileImageUploader s3ProfileImageUploader;

    @Test
    public void imageDownAndUpload() throws IOException {
        //! 이미지 다운로드
        String imageURL = "https://wokat-default-image.s3.ap-northeast-2.amazonaws.com/default-profileImage.png";
        String fileName = "temp.png";

        File downloadedFile = s3ProfileImageUploader.downloadImage(imageURL, fileName);
        Path filePath = downloadedFile.toPath();
        InputStream inputStream = Files.newInputStream(filePath);

        MultipartFile multipartFile = new MockMultipartFile(downloadedFile.getName(), downloadedFile.getName(), "png", inputStream);

        //! S3 업로드
        String storedFileName = "test.png";
        String S3ImageURL = s3ProfileImageUploader.uploadObject(multipartFile, storedFileName);

        System.out.println("image URL: " + S3ImageURL);

        //! 업로드 삭제
        s3ProfileImageUploader.deleteObject(storedFileName);

    }

}
