package com.example.spring_etrees.domain.reply;

import com.example.spring_etrees.domain.board.Board;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReplyTest {

    @Test
    void create() {
        Board board = Board.create("타이틀", "코멘트");

        Reply reply = Reply.create(board, "댓글 내용");

        assertThat(reply.getCreator()).isEqualTo("SYSTEM");
        assertThat(reply.getModifier()).isEqualTo("SYSTEM");
    }

    @Test
    void update() {

        Board board = Board.create("타이틀", "코멘트");

        Reply reply = Reply.create(board, "댓글 내용");

        reply.update("수정된 댓글 내용");

        assertThat(reply.getReplyContent()).isEqualTo("수정된 댓글 내용");
    }

}