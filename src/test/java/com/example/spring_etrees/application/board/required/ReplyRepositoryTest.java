package com.example.spring_etrees.application.board.required;

import com.example.spring_etrees.application.reply.required.ReplyRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardCreateRequest;
import com.example.spring_etrees.domain.board.BoardFixture;
import com.example.spring_etrees.domain.board.BoardType;
import com.example.spring_etrees.domain.reply.Reply;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class ReplyRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private EntityManager em;

    @Test
    void 게시글삭제시_댓글들도_함께삭제된다() {
        // given - 게시글 생성
        Board board = BoardFixture.createBoard();
        Board savedBoard = boardRepository.save(board);

        // flush로 즉시 DB에 반영하고 영속성 컨텍스트 동기화
        em.flush();
        em.clear();

        // given - 댓글 3개 생성
        Reply reply1 = Reply.create(savedBoard, "첫 번째 댓글", "creator");
        Reply reply2 = Reply.create(savedBoard, "두 번째 댓글", "creator");
        Reply reply3 = Reply.create(savedBoard, "세 번째 댓글", "creator");

        replyRepository.save(reply1);
        replyRepository.save(reply2);
        replyRepository.save(reply3);

        // flush로 댓글들도 즉시 DB에 반영
        em.flush();
        em.clear();

        // 저장 확인
        assertThat(boardRepository.count()).isEqualTo(1);
        assertThat(replyRepository.count()).isEqualTo(3);

        // when - 게시글 삭제
        boardRepository.deleteById(savedBoard.getBoardNum());
        boardRepository.flush(); // 삭제도 즉시 반영

        // then - 게시글과 댓글 모두 삭제 확인
        assertThat(boardRepository.count()).isEqualTo(0);
        assertThat(replyRepository.count()).isEqualTo(0);
    }

    @Test
    void 다른게시글의_댓글은_삭제되지않는다() {
        // given - 두 개의 게시글 생성
        Board board1 = BoardFixture.createBoard();
        Board board2 = BoardFixture.createBoard(BoardType.GENERAL, "두 번째 게시글", "내용2");
        Board savedBoard1 = boardRepository.save(board1);
        Board savedBoard2 = boardRepository.save(board2);

        // given - 각각에 댓글 생성
        Reply reply1 = Reply.create(savedBoard1, "첫 번째 게시글의 댓글", "creator");
        Reply reply2 = Reply.create(savedBoard2, "두 번째 게시글의 댓글1", "creator");
        Reply reply3 = Reply.create(savedBoard2, "두 번째 게시글의 댓글2", "creator");

        replyRepository.save(reply1);
        replyRepository.save(reply2);
        replyRepository.save(reply3);

        em.flush();
        em.clear();

        // 저장 확인
        assertThat(boardRepository.count()).isEqualTo(2);
        assertThat(replyRepository.count()).isEqualTo(3);

        // when - 첫 번째 게시글만 삭제
        boardRepository.deleteById(savedBoard1.getBoardNum());

        em.flush();
        em.clear();

        // then - 첫 번째 게시글과 관련 댓글만 삭제, 두 번째 게시글 댓글은 유지
        assertThat(boardRepository.count()).isEqualTo(1);
        assertThat(replyRepository.count()).isEqualTo(2);

        // 남은 게시글이 두 번째 게시글인지 확인
        Board remainingBoard = boardRepository.findAll().get(0);
        assertThat(remainingBoard.getBoardTitle()).isEqualTo("두 번째 게시글");
    }
}