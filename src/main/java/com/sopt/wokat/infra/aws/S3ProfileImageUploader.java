package com.sopt.wokat.infra.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3ProfileImageUploader {
    
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.buckets.bucket1}")
    public String bucket;

	//! 객체 업로드 
    public String uploadObject(MultipartFile multipartFile, String storedFileName) throws IOException {
		String filePath = "kakao/" + storedFileName;

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentDisposition("inline");

		amazonS3Client.putObject(new PutObjectRequest(bucket, filePath, multipartFile.getInputStream(), metadata));
		
		return amazonS3Client.getUrl(bucket, filePath).toString();
	}

	//! 객체 삭제
	public void deleteObject(String storedFileName) throws AmazonServiceException {
		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, "kakao/" + storedFileName));
	}

	//! URL로부터 이미지 다운로드
    public File downloadImage(String imageUrl, String fileName) {
		try {
			URL url = new URL(imageUrl);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(fileName);
			
			byte[] buffer = new byte[1024*16];
			
			while (true) {
				int inputData = is.read(buffer);
				if(inputData == -1) break;
				os.write(buffer, 0, inputData);
			}

			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return new File(fileName);
	}

}
