package com.example.spring_etrees.application.file;

import com.example.spring_etrees.application.file.provided.FileModifier;
import com.example.spring_etrees.application.file.required.FileRepository;
import com.example.spring_etrees.application.file.required.FileUploader;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.file.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileModifyService implements FileModifier {

    private final FileUploader fileUploader;
    private final FileRepository fileRepository;

    @Override
    public List<File> uploadAndSaveFiles(List<MultipartFile> files, Board board) {
        List<File> savedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // 1. S3 업로드
                String fileUrl = fileUploader.uploadFile(file);
                String storedFileName = fileUploader.extractFileNameFromUrl(fileUrl);

                // 2. 파일 확장자 추출
                String fileExtension = getFileExtension(file.getOriginalFilename());

                // 3. File 엔티티 생성 및 저장
                File fileEntity = File.create(
                        file.getOriginalFilename(),
                        storedFileName,
                        fileUrl,
                        file.getSize(),
                        fileExtension,
                        board
                );

                File savedFile = fileRepository.save(fileEntity);
                savedFiles.add(savedFile);
            }
        }

        return savedFiles;
    }

    // TODO: S3와 DB 트랜잭션 불일치 문제 해결 필요
    // 문제: DB는 롤백되지만 S3 파일은 이미 삭제/업로드된 상태로 남을 수 있음
    // 해결 방안 고려사항:
    // 1. 보상 트랜잭션 패턴 (Saga Pattern)
    // 2. 이벤트 기반 비동기 처리
    // 3. 처리 순서 변경 (DB 먼저, S3 나중에)
    // 4. 분산 트랜잭션 (2PC) - 성능상 비추천
    // 5. 최종 일관성(Eventually Consistent) 접근
    @Override
    public void updateBoardFiles(Board board, List<Long> deleteFileIds, List<MultipartFile> newFiles) {
        // 1. 삭제할 파일들 처리
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            deleteFiles(deleteFileIds);
        }

        // 2. 새 파일들 추가
        if (newFiles != null && !newFiles.isEmpty()) {
            uploadAndSaveFiles(newFiles, board);
        }
    }

    @Override
    public void deleteFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

        // 1. S3에서 파일 삭제
        String fileName = fileUploader.extractFileNameFromUrl(file.getFileUrl());
        fileUploader.deleteFile(fileName);

        // 2. DB에서 파일 삭제
        fileRepository.delete(file);
    }

    @Override
    public void deleteFiles(List<Long> fileIds) {
        for (Long fileId : fileIds) {
            deleteFile(fileId); // 기존 메서드 재활용
        }
    }

    @Override
    public void deleteAllFilesByBoard(Board board) {
        List<File> files = fileRepository.findByBoard(board);

        for (File file : files) {
            // 1. S3에서 파일 삭제
            String fileName = fileUploader.extractFileNameFromUrl(file.getFileUrl());
            fileUploader.deleteFile(fileName);
        }

        // 2. DB에서 모든 파일 삭제
        fileRepository.deleteAll(files);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}