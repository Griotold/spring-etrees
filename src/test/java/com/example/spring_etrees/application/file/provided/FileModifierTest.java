package com.example.spring_etrees.application.file.provided;

import com.example.spring_etrees.SpringEtreesTestConfiguration;
import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.application.file.required.FileRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardFixture;
import com.example.spring_etrees.domain.file.File;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@Import(SpringEtreesTestConfiguration.class)
@SpringBootTest
record FileModifierTest(FileModifier fileModifier,
                        BoardRepository boardRepository,
                        FileRepository fileRepository,
                        EntityManager entityManager) {

    @Test
    void uploadAndSaveFiles() {
        Board board = boardRepository.save(BoardFixture.createBoard());

        MultipartFile file1 = createMockMultipartFile("file1", "test1.jpg", "image/jpeg", "파일 내용 1");
        MultipartFile file2 = createMockMultipartFile("file2", "test2.jpg", "image/jpeg", "파일 내용 2");
        List<MultipartFile> files = List.of(file1, file2);

        // 테스트 대상 메서드 호출
        List<File> savedFiles = fileModifier.uploadAndSaveFiles(files, board);
        assertThat(savedFiles.size()).isEqualTo(2);

        entityManager.flush();
        entityManager.clear();

        long count = fileRepository.count();
        assertThat(count).isEqualTo(2L);
    }

    @Test
    void uploadAndSaveFiles_빈_파일일때() {
        var board = boardRepository.save(BoardFixture.createBoard());

        List<MultipartFile> files = List.of();

        List<File> saveFiles = fileModifier.uploadAndSaveFiles(files, board);
        assertThat(saveFiles).isEmpty();
    }

    @Test
    void updateBoardFiles_파일삭제후_새파일추가() {
        Board board = boardRepository.save(BoardFixture.createBoard());

        MultipartFile oldFile = createMockMultipartFile("old.jpg", "old.jpg", "image/jpeg", "old file content");
        List<File> oldFiles = fileModifier.uploadAndSaveFiles(List.of(oldFile), board);
        Long deleteFileId = oldFiles.get(0).getId();

        // 새로 업로드할 파일 생성
        MultipartFile newFile = createMockMultipartFile("new.jpg", "new.jpg", "image/jpeg", "new file content");

        // 업데이트 수행 - 기존 파일 삭제, 새 파일 추가
        fileModifier.updateBoardFiles(board, List.of(deleteFileId), List.of(newFile));

        entityManager.flush();
        entityManager.clear();

        // 삭제된 파일은 없어야 하고, 새 파일은 저장되어야 함 검증
        assertThat(fileRepository.existsById(deleteFileId)).isFalse();
        List<File> files = fileRepository.findByBoard(board);
        assertThat(files).hasSize(1);
        assertThat(files.get(0).getOriginalFileName()).isEqualTo("new.jpg");
    }

    @Test
    void updateBoardFiles_빈_입력일때() {
        Board board = boardRepository.save(BoardFixture.createBoard());

        // 빈 리스트 전달
        fileModifier.updateBoardFiles(board, List.of(), List.of());

        // 기존 파일도 없어서 영향 없음 검증
        List<File> files = fileRepository.findByBoard(board);
        assertThat(files).isEmpty();
    }

    @Test
    void deleteFile_기존파일_삭제() {
        Board board = boardRepository.save(BoardFixture.createBoard());

        MultipartFile file = createMockMultipartFile("toDelete.jpg", "toDelete.jpg", "image/jpeg", "content");
        List<File> savedFiles = fileModifier.uploadAndSaveFiles(List.of(file), board);
        Long fileId = savedFiles.get(0).getId();

        fileModifier.deleteFile(fileId);

        entityManager.flush();
        entityManager.clear();

        assertThat(fileRepository.existsById(fileId)).isFalse();
    }

    @Test
    void deleteFiles_여러파일_삭제() {
        Board board = boardRepository.save(BoardFixture.createBoard());

        MultipartFile file1 = createMockMultipartFile("file1.jpg", "file1.jpg", "image/jpeg", "content1");
        MultipartFile file2 = createMockMultipartFile("file2.jpg", "file2.jpg", "image/jpeg", "content2");
        List<File> savedFiles = fileModifier.uploadAndSaveFiles(List.of(file1, file2), board);
        List<Long> fileIds = savedFiles.stream().map(File::getId).toList();

        fileModifier.deleteFiles(fileIds);

        entityManager.flush();
        entityManager.clear();

        assertThat(fileRepository.findAllById(fileIds)).isEmpty();
    }

    @Test
    void deleteAllFilesByBoard_모든파일_삭제() {
        Board board = boardRepository.save(BoardFixture.createBoard());

        MultipartFile file1 = createMockMultipartFile("file1.jpg", "file1.jpg", "image/jpeg", "content1");
        MultipartFile file2 = createMockMultipartFile("file2.jpg", "file2.jpg", "image/jpeg", "content2");
        fileModifier.uploadAndSaveFiles(List.of(file1, file2), board);

        List<File> beforeDelete = fileRepository.findByBoard(board);
        assertThat(beforeDelete).hasSize(2);

        fileModifier.deleteAllFilesByBoard(board);

        entityManager.flush();
        entityManager.clear();

        List<File> afterDelete = fileRepository.findByBoard(board);
        assertThat(afterDelete).isEmpty();
    }

    private MultipartFile createMockMultipartFile(String name, String originalFilename, String contentType, String content) {
        return new MockMultipartFile(
                name,
                originalFilename,
                contentType,
                content.getBytes()
        );
    }
}