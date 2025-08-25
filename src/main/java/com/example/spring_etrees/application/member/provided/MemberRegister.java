package com.example.spring_etrees.application.member.provided;

import com.example.spring_etrees.domain.member.Member;
import com.example.spring_etrees.domain.member.MemberCreateRequest;
import com.example.spring_etrees.domain.member.MemberInfoUpdateRequest;

public interface MemberRegister {

    /**
     * 회원 가입
     * @param createRequest 회원가입 요청
     * @return 생성된 회원
     */
    Member registerMember(MemberCreateRequest createRequest);

    /**
     * 회원 수정
     * @param updateRequest 회원수정 요청
     * @return 수정된 회원
     * */
    Member updateInfoMember(MemberInfoUpdateRequest updateRequest, Long memberId);

    /**
     * 관리자 승격
     * @param targetMemberId 승격 대상 회원 ID
     * @return 승격된 회원
     */
    Member promoteToAdmin(Long targetMemberId);

    /**
     * 일반회원 강등
     * @param targetMemberId 강등 대상 회원 ID
     * @return 강등된 회원
     */
    Member demoteToUser(Long targetMemberId);
}
