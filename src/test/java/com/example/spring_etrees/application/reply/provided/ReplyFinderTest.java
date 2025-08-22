package com.example.spring_etrees.application.reply.provided;

import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.application.reply.required.ReplyRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardFixture;
import com.example.spring_etrees.domain.reply.Reply;
import com.example.spring_etrees.domain.reply.ReplyFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
record ReplyFinderTest(ReplyFinder replyFinder,
                       ReplyRepository replyRepository,
                       BoardRepository boardRepository,
                       EntityManager entityManager) {

    @Test
    void getRepliesByBoard() {
        // given
        Board board = boardRepository.save(BoardFixture.createBoard());
        Reply reply1 = replyRepository.save(ReplyFixture.createReply(board, "첫 번째 댓글"));
        Reply reply2 = replyRepository.save(ReplyFixture.createReply(board, "두 번째 댓글"));
        Reply reply3 = replyRepository.save(ReplyFixture.createReply(board, "세 번째 댓글"));

        entityManager.flush();
        entityManager.clear();

        // when
        List<Reply> replies = replyFinder.getRepliesByBoard(board.getBoardNum());

        // then
        assertThat(replies).hasSize(3);
        assertThat(replies.get(0).getReplyContent()).isEqualTo("첫 번째 댓글");
        assertThat(replies.get(1).getReplyContent()).isEqualTo("두 번째 댓글");
        assertThat(replies.get(2).getReplyContent()).isEqualTo("세 번째 댓글");

        // 생성 시간 순으로 정렬되는지 확인
        assertThat(replies.get(0).getCreateTime()).isBeforeOrEqualTo(replies.get(1).getCreateTime());
        assertThat(replies.get(1).getCreateTime()).isBeforeOrEqualTo(replies.get(2).getCreateTime());
    }

    @Test
    void getRepliesByBoard_댓글이없는경우() {
        // given
        Board board = boardRepository.save(BoardFixture.createBoard());

        entityManager.flush();
        entityManager.clear();

        // when
        List<Reply> replies = replyFinder.getRepliesByBoard(board.getBoardNum());

        // then
        assertThat(replies).isEmpty();
    }

    @Test
    void getRepliesByBoard_존재하지않는게시글() {
        // when
        List<Reply> replies = replyFinder.getRepliesByBoard(999L);

        // then
        assertThat(replies).isEmpty();
    }

    @Test
    void getReply() {
        // given
        Board board = boardRepository.save(BoardFixture.createBoard());
        Reply reply = replyRepository.save(ReplyFixture.createReply(board));

        entityManager.flush();
        entityManager.clear();

        // when
        Reply result = replyFinder.getReply(reply.getReplyNum());

        // then
        assertThat(result.getReplyNum()).isEqualTo(reply.getReplyNum());
        assertThat(result.getReplyContent()).isEqualTo(reply.getReplyContent());
        assertThat(result.getCreator()).isEqualTo(reply.getCreator());
    }

    @Test
    void getReply_댓글이없을때() {
        // when & then
        assertThatThrownBy(() -> replyFinder.getReply(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 댓글이 존재하지 않습니다. id=999");
    }

    @Test
    void getRepliesByBoard_여러게시글의댓글구분() {
        // given
        Board board1 = boardRepository.save(BoardFixture.createBoard());
        Board board2 = boardRepository.save(BoardFixture.createBoard());

        replyRepository.save(ReplyFixture.createReply(board1, "게시글1의 댓글1"));
        replyRepository.save(ReplyFixture.createReply(board1, "게시글1의 댓글2"));
        replyRepository.save(ReplyFixture.createReply(board2, "게시글2의 댓글1"));

        entityManager.flush();
        entityManager.clear();

        // when
        List<Reply> board1Replies = replyFinder.getRepliesByBoard(board1.getBoardNum());
        List<Reply> board2Replies = replyFinder.getRepliesByBoard(board2.getBoardNum());

        // then
        assertThat(board1Replies).hasSize(2);
        assertThat(board2Replies).hasSize(1);

        assertThat(board1Replies.get(0).getReplyContent()).isEqualTo("게시글1의 댓글1");
        assertThat(board1Replies.get(1).getReplyContent()).isEqualTo("게시글1의 댓글2");
        assertThat(board2Replies.get(0).getReplyContent()).isEqualTo("게시글2의 댓글1");
    }
}