package com.example.spring_etrees.application.board.provided;

import com.example.spring_etrees.application.board.BoardModifyService;
import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.domain.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardModifierTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardModifyService boardModifyService;

    // 인터페이스 참조
    private BoardModifier boardModifier;
    private BoardCreator boardCreator;

    @BeforeEach
    void setUp() {
        // 구현체를 인터페이스로 업캐스팅
        boardModifier = boardModifyService;
        boardCreator = boardModifyService;
    }

    @Test
    void createBoard_성공() {
        // given
        BoardCreateRequest request = new BoardCreateRequest(BoardType.GENERAL, "테스트 제목", "테스트 내용");
        String creator = "testUser";
        Board savedBoard = BoardFixture.createBoard(creator);
        // boardNum을 설정하기 위해 reflection 사용 (실제로는 DB에서 자동 생성)
        setField(savedBoard, "boardNum", 1L);

        given(boardRepository.save(any(Board.class))).willReturn(savedBoard);

        // when - 인터페이스로 테스트 (creator 파라미터 추가)
        Board result = boardCreator.createBoard(request, creator);

        // then
        assertThat(result.getBoardNum()).isEqualTo(1L);
        then(boardRepository).should().save(any(Board.class));
    }

    @Test
    void updateBoard_성공() {
        // given
        Long boardNum = 1L;
        BoardUpdateRequest request = new BoardUpdateRequest("수정된 제목", "수정된 내용");
        String creator = "originalCreator";
        String modifier = "originalCreator"; // 작성자와 동일한 사용자
        Board existingBoard = BoardFixture.createBoard(creator);
        setField(existingBoard, "boardNum", boardNum);

        given(boardRepository.findById(boardNum)).willReturn(Optional.of(existingBoard));

        // when - 인터페이스로 테스트
        boardModifier.updateBoard(boardNum, request, modifier);

        // then
        assertThat(existingBoard.getBoardTitle()).isEqualTo("수정된 제목");
        assertThat(existingBoard.getBoardComment()).isEqualTo("수정된 내용");
        assertThat(existingBoard.getModifier()).isEqualTo("originalCreator"); // 작성자와 동일

        then(boardRepository).should().findById(boardNum);
        then(boardRepository).should().save(existingBoard);
    }

    @Test
    void updateBoard_존재하지않는게시글_예외발생() {
        // given
        Long boardNum = 999L;
        BoardUpdateRequest request = BoardFixture.createBoardUpdateRequest();
        String modifier = "testModifier";

        given(boardRepository.findById(boardNum)).willReturn(Optional.empty());

        // when & then - 인터페이스로 테스트 (modifier 파라미터 추가)
        assertThatThrownBy(() -> boardModifier.updateBoard(boardNum, request, modifier))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다. id=" + boardNum);

        then(boardRepository).should().findById(boardNum);
        then(boardRepository).should(never()).save(any(Board.class));
    }

    @Test
    void createBoard_도메인메서드호출확인() {
        // given
        BoardCreateRequest request = BoardFixture.createBoardCreateRequest();
        String creator = "testCreator";
        Board savedBoard = BoardFixture.createBoard(creator);
        setField(savedBoard, "boardNum", 1L);

        given(boardRepository.save(any(Board.class))).willReturn(savedBoard);

        // when - 인터페이스로 테스트 (creator 파라미터 추가)
        boardCreator.createBoard(request, creator);

        // then
        then(boardRepository).should().save(argThat(board ->
                board.getBoardTitle().equals("테스트 제목") &&          // Fixture의 실제 값
                        board.getBoardComment().equals("테스트 내용") &&  // Fixture의 실제 값
                        board.getBoardType().equals(BoardType.GENERAL) && // Enum으로 비교
                        board.getCreator().equals("testCreator") &&      // 변경된 값
                        board.getModifier().equals("testCreator")        // 변경된 값 (생성자와 동일)
        ));
    }

    @Test
    void deleteBoard_성공() {
        // given
        Long boardNum = 1L;
        String requester = "testUser";
        Board existingBoard = BoardFixture.createBoard(requester); // 같은 사용자로 생성
        setField(existingBoard, "boardNum", boardNum);

        given(boardRepository.findById(boardNum)).willReturn(Optional.of(existingBoard));

        // when - 인터페이스로 테스트 (requester 파라미터 추가)
        boardModifier.deleteBoard(boardNum, requester);

        // then
        then(boardRepository).should().findById(boardNum);
        then(boardRepository).should().deleteById(boardNum); // delete → deleteById로 변경
    }

    @Test
    void deleteBoard_존재하지않는게시글_예외발생() {
        // given
        Long boardNum = 999L;
        String requester = "testUser";

        given(boardRepository.findById(boardNum)).willReturn(Optional.empty());

        // when & then - 인터페이스로 테스트 (requester 파라미터 추가)
        assertThatThrownBy(() -> boardModifier.deleteBoard(boardNum, requester))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다. id=" + boardNum);

        then(boardRepository).should().findById(boardNum);
        then(boardRepository).should(never()).deleteById(any(Long.class)); // delete → deleteById로 변경
    }

    @Test
    void deleteBoard_작성자가아님_예외발생() {
        // given
        Long boardNum = 1L;
        String creator = "originalUser";
        String requester = "anotherUser"; // 다른 사용자
        Board existingBoard = BoardFixture.createBoard(creator);
        setField(existingBoard, "boardNum", boardNum);

        given(boardRepository.findById(boardNum)).willReturn(Optional.of(existingBoard));

        // when & then
        assertThatThrownBy(() -> boardModifier.deleteBoard(boardNum, requester))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 작성자만 수정/삭제할 수 있습니다.");

        then(boardRepository).should().findById(boardNum);
        then(boardRepository).should(never()).deleteById(any(Long.class));
    }

    // Reflection을 사용한 필드 설정 헬퍼 메서드
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}