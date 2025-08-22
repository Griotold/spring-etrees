package com.example.spring_etrees.application.reply.provided;

import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.application.reply.required.ReplyRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardFixture;
import com.example.spring_etrees.domain.reply.Reply;
import com.example.spring_etrees.domain.reply.ReplyFixture;
import com.example.spring_etrees.domain.reply.ReplyUpdateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
record ReplyModifierTest(ReplyModifier replyModifier,
                         ReplyCreator replyCreator,
                         ReplyRepository replyRepository,
                         BoardRepository boardRepository,
                         EntityManager entityManager) {

    @Test
    void updateReply() {
        Board board = boardRepository.save(BoardFixture.createBoard());
        Reply reply = replyRepository.save(ReplyFixture.createReply(board));
        ReplyUpdateRequest updateRequest = ReplyFixture.createReplyUpdateRequest();
        String modifier = "수정한 사람";

        replyModifier.updateReply(reply.getReplyNum(), updateRequest, modifier);

        entityManager.flush();
        entityManager.clear();

        Reply result = replyRepository.findById(reply.getReplyNum()).get();
        assertThat(result.getReplyContent()).isEqualTo("수정할 댓글 내용");
        assertThat(result.getModifier()).isEqualTo(modifier);
    }

    @Test
    void updateReply_댓글이없을때() {
        ReplyUpdateRequest updateRequest = ReplyFixture.createReplyUpdateRequest();
        String modifier = "수정한 사람";

        assertThatThrownBy(() -> replyModifier.updateReply(999L, updateRequest, modifier))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteReply() {
        Board board = boardRepository.save(BoardFixture.createBoard());
        Reply reply = replyRepository.save(ReplyFixture.createReply(board));

        replyModifier.deleteReply(reply.getReplyNum());

        entityManager.flush();
        entityManager.clear();

        assertThat(replyRepository.findById(reply.getReplyNum())).isEmpty();
    }

    @Test
    void deleteReply_없는댓글일때() {
        assertThatThrownBy(() -> replyModifier.deleteReply(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}