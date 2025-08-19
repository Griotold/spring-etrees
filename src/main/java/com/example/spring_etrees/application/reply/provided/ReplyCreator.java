package com.example.spring_etrees.application.reply.provided;

import com.example.spring_etrees.domain.reply.ReplyCreateRequest;

public interface ReplyCreator {

    /**
     * 댓글 생성
     * @param request 댓글 생성 요청
     * @return 생성된 댓글 번호
     */
    Long createReply(ReplyCreateRequest request);
}
