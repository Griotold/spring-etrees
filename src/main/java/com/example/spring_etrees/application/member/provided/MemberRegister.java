package com.example.spring_etrees.application.member.provided;

import com.example.spring_etrees.domain.member.MemberCreateRequest;

public interface MemberRegister {

    /**
     * 회원 가입
     * @param request 회원가입 요청
     * @return 생성된 회원 ID
     */
    Long registerMember(MemberCreateRequest request);
}
