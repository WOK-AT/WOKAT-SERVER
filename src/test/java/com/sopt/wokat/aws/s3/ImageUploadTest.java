package com.sopt.wokat.aws.s3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sopt.wokat.domain.member.dto.ProfileImageUploadDTO;
import com.sopt.wokat.infra.aws.S3ProfileUploader;

@SpringBootTest
public class ImageUploadTest {
    
    @Autowired
    private S3ProfileUploader downlaodFileS3Uploader;

    @Test
    public void imageDownAndUpload() throws IOException {
        //! 이미지 다운로드
        String imageURL = "https://wokat-default-image.s3.ap-northeast-2.amazonaws.com/test.png";
        
        //! S3 업로드
        ProfileImageUploadDTO imageUploadDTO = downlaodFileS3Uploader.uploadS3ProfileImage(imageURL);
        System.out.println("image URL: " + imageUploadDTO.getS3ImageURL());

        //! 업로드 삭제
        downlaodFileS3Uploader.deleteObject(imageUploadDTO.getFileName());
    }

}
