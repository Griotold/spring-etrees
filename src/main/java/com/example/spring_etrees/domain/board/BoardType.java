package com.example.spring_etrees.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardType {
    GENERAL("a01", "일반"),
    QNA("a02", "Q&A"),
    ANONYMOUS("a03", "익명"),
    FREE("a04", "자유");

    private final String code;
    private final String description;
}