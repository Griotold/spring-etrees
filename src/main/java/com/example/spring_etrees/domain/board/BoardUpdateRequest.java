package com.example.spring_etrees.domain.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardUpdateRequest(
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자 이하로 입력해주세요")
        String boardTitle,

        @NotBlank(message = "내용은 필수입니다")
        String boardComment
) {
}
