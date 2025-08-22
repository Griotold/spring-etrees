package com.example.spring_etrees.domain.reply;

import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReplyTest {

    @Test
    void create() {
        Board board = BoardFixture.createBoard();
        String creator = "Leo";
        Reply reply = Reply.create(board, "댓글 내용", creator);

        assertThat(reply.getCreator()).isEqualTo(creator);
        assertThat(reply.getModifier()).isEqualTo(creator);
    }

    @Test
    void update() {
        Board board = BoardFixture.createBoard();
        String creator = "Leo";
        Reply reply = Reply.create(board, "댓글 내용", creator);

        String modifier = reply.getModifier();
        reply.update("수정된 댓글 내용", modifier);

        assertThat(reply.getReplyContent()).isEqualTo("수정된 댓글 내용");
    }

}