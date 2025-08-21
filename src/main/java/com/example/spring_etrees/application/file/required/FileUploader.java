package com.example.spring_etrees.application.file.required;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    /**
     * 파일 업로드
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URL
     */
    String uploadFile(MultipartFile file);

    /**
     * 파일 삭제
     * @param fileName 삭제할 파일명
     */
    void deleteFile(String fileName);

    /**
     * URL에서 파일명 추출
     * @param fileUrl 파일 URL
     * @return 파일명
     */
    String extractFileNameFromUrl(String fileUrl);
}