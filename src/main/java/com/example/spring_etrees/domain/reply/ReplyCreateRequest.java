package com.example.spring_etrees.domain.reply;

public record ReplyCreateRequest(
        Long boardNum,
        String replyContent
) {
}
