package com.example.spring_etrees.application.reply.provided;

import com.example.spring_etrees.domain.reply.Reply;

import java.util.List;

public interface ReplyFinder {

    /**
     * 특정 게시글의 댓글 목록 조회
     * @param boardNum 게시글 번호
     * @return 댓글 목록 (작성 순서대로)
     */
    List<Reply> getRepliesByBoard(Long boardNum);

    /**
     * 댓글 단일 조회
     * @param replyNum 댓글 번호
     * @return 댓글 정보
     */
    Reply getReply(Long replyNum);
}
