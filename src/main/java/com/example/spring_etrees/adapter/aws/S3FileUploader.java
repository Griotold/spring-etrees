package com.example.spring_etrees.adapter.aws;

import com.example.spring_etrees.application.file.required.FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3FileUploader implements FileUploader {

    private final S3Service s3Service;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return s3Service.uploadFile(file);
        } catch (Exception e) {
            log.error("S3 파일 업로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            s3Service.deleteFile(fileName);
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 삭제에 실패했습니다.", e);
        }
    }

    @Override
    public String extractFileNameFromUrl(String fileUrl) {
        return s3Service.extractFileNameFromUrl(fileUrl);
    }
}