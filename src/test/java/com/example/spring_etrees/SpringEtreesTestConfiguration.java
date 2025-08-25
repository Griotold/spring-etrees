package com.example.spring_etrees;

import com.example.spring_etrees.application.file.required.FileUploader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

@TestConfiguration
public class SpringEtreesTestConfiguration {

    @Bean
    public FileUploader fileUploader() {
        return new FileUploader() {
            @Override
            public String uploadFile(MultipartFile file) {
                // 업로드된 파일 URL을 임의로 반환
                return "https://fake-s3.com/" + file.getOriginalFilename();
            }

            @Override
            public void deleteFile(String fileName) {
                // 아무 동작도 하지 않거나, log만 남김
            }

            @Override
            public String extractFileNameFromUrl(String fileUrl) {
                // 간단히 마지막 슬래시 뒤를 반환
                return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            }
        };
    }

}
