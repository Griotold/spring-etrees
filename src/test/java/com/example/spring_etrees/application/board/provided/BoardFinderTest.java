package com.example.spring_etrees.application.board.provided;

import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardCreateRequest;
import com.example.spring_etrees.domain.board.BoardType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BoardFinderTest {

    @Autowired
    private BoardFinder boardFinder;

    @Autowired
    private EntityManager entityManager;

    private Board generalBoard1;
    private Board generalBoard2;
    private Board qnaBoard1;
    private Board freeBoard1;
    private Board anonymousBoard1;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 생성
        generalBoard1 = Board.create(
                new BoardCreateRequest(BoardType.GENERAL, "일반 게시글 1", "내용 1"),
                "작성자1"
        );
        generalBoard2 = Board.create(
                new BoardCreateRequest(BoardType.GENERAL, "일반 게시글 2", "내용 2"),
                "작성자2"
        );
        qnaBoard1 = Board.create(
                new BoardCreateRequest(BoardType.QNA, "Q&A 게시글 1", "질문 내용"),
                "작성자3"
        );
        freeBoard1 = Board.create(
                new BoardCreateRequest(BoardType.FREE, "자유 게시글 1", "자유 내용"),
                "작성자4"
        );
        anonymousBoard1 = Board.create(
                new BoardCreateRequest(BoardType.ANONYMOUS, "익명 게시글 1", "익명 내용"),
                "작성자5"
        );

        // DB에 저장
        entityManager.persist(generalBoard1);
        entityManager.persist(generalBoard2);
        entityManager.persist(qnaBoard1);
        entityManager.persist(freeBoard1);
        entityManager.persist(anonymousBoard1);
        entityManager.flush();
    }

    @Test
    @DisplayName("전체 게시글 조회 - all 포함")
    void getBoardListByTypes_withAll() {
        // given
        List<String> types = Arrays.asList("all", "GENERAL");
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Board> result = boardFinder.getBoardListByTypes(types, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(5);
    }

    @Test
    @DisplayName("전체 게시글 조회 - 빈 리스트")
    void getBoardListByTypes_withEmptyList() {
        // given
        List<String> types = Collections.emptyList();
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Board> result = boardFinder.getBoardListByTypes(types, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(5);
    }

    @Test
    @DisplayName("특정 타입만 조회 - GENERAL")
    void getBoardListByTypes_withSingleType() {
        // given
        List<String> types = List.of("GENERAL");
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Board> result = boardFinder.getBoardListByTypes(types, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .allMatch(board -> board.getBoardType() == BoardType.GENERAL);
    }

    @Test
    @DisplayName("여러 타입 조회 - GENERAL, QNA")
    void getBoardListByTypes_withMultipleTypes() {
        // given
        List<String> types = Arrays.asList("GENERAL", "QNA");
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Board> result = boardFinder.getBoardListByTypes(types, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .allMatch(board ->
                        board.getBoardType() == BoardType.GENERAL ||
                                board.getBoardType() == BoardType.QNA
                );
    }

    @Test
    @DisplayName("잘못된 타입 포함 - 유효한 타입만 처리")
    void getBoardListByTypes_withInvalidType() {
        // given
        List<String> types = Arrays.asList("GENERAL", "INVALID_TYPE", "QNA");
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Board> result = boardFinder.getBoardListByTypes(types, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .allMatch(board ->
                        board.getBoardType() == BoardType.GENERAL ||
                                board.getBoardType() == BoardType.QNA
                );
    }

    @Test
    @DisplayName("모든 타입이 잘못된 경우 - 전체 조회")
    void getBoardListByTypes_withAllInvalidTypes() {
        // given
        List<String> types = Arrays.asList("INVALID1", "INVALID2");
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Board> result = boardFinder.getBoardListByTypes(types, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(5);
    }

    @Test
    @DisplayName("페이징 테스트")
    void getBoardListByTypes_withPaging() {
        // given
        List<String> types = List.of("GENERAL");
        PageRequest pageRequest = PageRequest.of(0, 1); // 1개씩

        // when
        Page<Board> result = boardFinder.getBoardListByTypes(types, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 번호 내림차순 정렬 확인")
    void getBoardListByTypes_orderByBoardNumDesc() {
        // given
        List<String> types = List.of("GENERAL");
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Board> result = boardFinder.getBoardListByTypes(types, pageRequest);

        // then
        List<Long> boardNums = result.getContent().stream()
                .map(Board::getBoardNum)
                .toList();

        assertThat(boardNums).isSortedAccordingTo((a, b) -> b.compareTo(a)); // 내림차순
    }

    @Test
    @DisplayName("존재하는 게시글 조회")
    void getBoard_success() {
        // when
        Board result = boardFinder.getBoard(generalBoard1.getBoardNum());

        // then
        assertThat(result.getBoardNum()).isEqualTo(generalBoard1.getBoardNum());
        assertThat(result.getBoardTitle()).isEqualTo("일반 게시글 1");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회시 예외 발생")
    void getBoard_notFound() {
        // given
        Long nonExistentId = 999L;

        // when & then
        assertThatThrownBy(() -> boardFinder.getBoard(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 게시글이 존재하지 않습니다");
    }
}