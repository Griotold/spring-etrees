package com.example.spring_etrees.adapter.aws;

import com.example.spring_etrees.application.file.required.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3FileUploader implements FileUploader {

    private final S3Service s3Service;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return s3Service.uploadFile(file);
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        s3Service.deleteFile(fileName);
    }

    @Override
    public String extractFileNameFromUrl(String fileUrl) {
        return s3Service.extractFileNameFromUrl(fileUrl);
    }
}