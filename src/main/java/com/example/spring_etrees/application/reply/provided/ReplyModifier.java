package com.example.spring_etrees.application.reply.provided;

import com.example.spring_etrees.domain.reply.ReplyUpdateRequest;

public interface ReplyModifier {

    /**
     * 댓글 수정
     * @param replyNum 댓글 번호
     * @param request 댓글 수정 요청
     */
    void updateReply(Long replyNum, ReplyUpdateRequest request);

    /**
     * 댓글 삭제
     * @param replyNum 댓글 번호
     */
    void deleteReply(Long replyNum);
}
