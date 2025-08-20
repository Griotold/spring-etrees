package com.example.spring_etrees.domain.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberCreateRequest(
        @NotBlank(message = "아이디는 필수입니다")
        @Size(min = 4, max = 20, message = "아이디는 4-20자 사이여야 합니다")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 6, max = 20, message = "비밀번호는 6-20자 사이여야 합니다")
        String password,

        @NotBlank(message = "비밀번호 확인은 필수입니다")
        String passwordCheck,

        @NotBlank(message = "이름은 필수입니다")
        @Size(max = 50, message = "이름은 50자 이하로 입력해주세요")
        String name,

        @NotBlank(message = "전화번호는 필수입니다")
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 xxx-xxxx-xxxx 형식으로 입력해주세요")
        String phone,

        String postNo,

        String address,

        String company
) {
    // 비밀번호 일치 검증
    public boolean isPasswordMatched() {
        return password != null && password.equals(passwordCheck);
    }
}