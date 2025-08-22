package com.example.spring_etrees.application.file.provided;

import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.file.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileModifier {

    /**
     * 파일 업로드 및 DB 저장 (글쓰기용)
     * @param files 업로드할 파일들
     * @param board 연관될 게시글
     * @return 저장된 파일 엔티티 목록
     */
    List<File> uploadAndSaveFiles(List<MultipartFile> files, Board board);

    /**
     * 게시글의 파일들을 수정 (수정용 - 삭제 + 추가)
     * @param board 게시글
     * @param deleteFileIds 삭제할 파일 ID 목록
     * @param newFiles 새로 추가할 파일 목록
     */
    void updateBoardFiles(Board board, List<Long> deleteFileIds, List<MultipartFile> newFiles);


    /**
     * 파일 삭제 (S3 + DB)
     * @param fileId 삭제할 파일 ID
     */
    void deleteFile(Long fileId);

    /**
     * 여러 파일 일괄 삭제
     * @param fileIds 삭제할 파일 ID 목록
     */
    void deleteFiles(List<Long> fileIds);

    /**
     * 게시글의 모든 파일 삭제
     * @param board 게시글
     */
    void deleteAllFilesByBoard(Board board);
}