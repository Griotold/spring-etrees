package com.example.spring_etrees.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("관리자"),
    USER("유저"),

    ;

    private final String description;
}
