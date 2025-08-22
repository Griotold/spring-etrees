package com.example.spring_etrees.domain.reply;

import com.example.spring_etrees.application.reply.provided.ReplyCreator;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardFixture;

public class ReplyFixture {


    public static ReplyUpdateRequest createReplyUpdateRequest() {
        return new ReplyUpdateRequest("수정할 댓글 내용");
    }

    public static Reply createReply(Board board, String replyContent) {
        return Reply.create(board, replyContent, "생성자");
    }

    public static Reply createReply(Board board) {
        return createReply(board, "댓글 내용");
    }
}
