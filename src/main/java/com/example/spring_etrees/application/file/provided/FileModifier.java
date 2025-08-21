package com.example.spring_etrees.application.file.provided;

import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.file.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileModifier {

    /**
     * 파일 업로드 및 DB 저장
     * @param files 업로드할 파일들
     * @param board 연관될 게시글
     * @return 저장된 파일 엔티티 목록
     */
    List<File> uploadAndSaveFiles(List<MultipartFile> files, Board board);

    /**
     * 파일 삭제 (S3 + DB)
     * @param fileId 삭제할 파일 ID
     */
    void deleteFile(Long fileId);

    /**
     * 게시글의 모든 파일 삭제
     * @param board 게시글
     */
    void deleteAllFilesByBoard(Board board);
}