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

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Import(SpringEtreesTestConfiguration.class)
@Transactional
record FileFinderTest(FileModifier fileModifier,
                      FileFinder fileFinder,
                      FileRepository fileRepository,
                      BoardRepository boardRepository,
                      EntityManager entityManager) {

    @Test
    void getFilesByBoard() {
        Board board = boardRepository.save(BoardFixture.createBoard());
        MultipartFile file = createMockMultipartFile("file1", "test1.jpg", "image/jpeg", "파일 내용 1");
        List<MultipartFile> files = List.of(file);
        List<File> savedFiles = fileModifier.uploadAndSaveFiles(files, board);

        entityManager.flush();
        entityManager.clear();

        List<File> found = fileFinder.getFilesByBoard(board);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.getFirst().getOriginalFileName()).isEqualTo(savedFiles.getFirst().getOriginalFileName());
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