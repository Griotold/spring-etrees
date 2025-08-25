package com.example.spring_etrees.domain.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdateRequest(
        @NotBlank @Size(max = 20) String name,
        @Size(max = 20) String phone,
        @Size(max = 10) String postNo,
        @Size(max = 200) String address,
        @Size(max = 100) String company
) {
}
