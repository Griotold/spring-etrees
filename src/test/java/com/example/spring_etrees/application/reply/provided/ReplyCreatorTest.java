package com.example.spring_etrees.application.reply.provided;

import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardFixture;
import com.example.spring_etrees.domain.reply.Reply;
import com.example.spring_etrees.domain.reply.ReplyCreateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
record ReplyCreatorTest(ReplyCreator replyCreator,
                        BoardRepository  boardRepository,
                        EntityManager entityManager) {

    @Test
    void createReply() {
        Board board = BoardFixture.createBoard();
        boardRepository.save(board);

        ReplyCreateRequest createRequest = new ReplyCreateRequest(board.getBoardNum(), "댓글 내용");
        String creator = "reply creator";

        Reply result = replyCreator.createReply(createRequest, creator);

        assertThat(result.getReplyNum()).isNotNull();
        assertThat(result.getBoard()).isEqualTo(board);
        assertThat(result.getReplyContent()).isEqualTo("댓글 내용");
    }

    @Test
    void createReply_보드가_없을때() {
        ReplyCreateRequest createRequest = new ReplyCreateRequest(999L, "댓글 내용");
        String creator = "reply creator";

        assertThatThrownBy(() -> replyCreator.createReply(createRequest, creator))
                .isInstanceOf(IllegalArgumentException.class);
    }
}