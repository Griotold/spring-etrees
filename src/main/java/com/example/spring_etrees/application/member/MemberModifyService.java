package com.example.spring_etrees.application.member;

import com.example.spring_etrees.application.member.provided.MemberRegister;
import com.example.spring_etrees.application.member.required.MemberRepository;
import com.example.spring_etrees.domain.member.*;
import groovyjarjarpicocli.CommandLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberModifyService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member registerMember(MemberCreateRequest createRequest) {
        validateMemberRegistration(createRequest);

        Member member = Member.register(createRequest, passwordEncoder);

        memberRepository.save(member);

        return member;
    }

    @Override
    public Member updateInfoMember(MemberInfoUpdateRequest updateRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. memberId: {} " + memberId));

        checkDuplicateName(updateRequest.name(), member);

        member.updateInfo(updateRequest);

        return memberRepository.save(member);
    }

    @Override
    public Member promoteToAdmin(Long targetMemberId) {
        Member member = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. memberId: {} " + targetMemberId));

        member.promoteToAdmin();

        return memberRepository.save(member);
    }

    @Override
    public Member demoteToUser(Long targetMemberId) {
        Member member = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. memberId: {} " + targetMemberId));

        member.demoteToUser();

        return memberRepository.save(member);
    }


    private void validateMemberRegistration(MemberCreateRequest request) {
        checkDuplicateUsername(request);
        checkDuplicateName(request.name());
        checkPasswordMatches(request);
    }

    private void checkDuplicateUsername(MemberCreateRequest request) {
        if (memberRepository.existsByUsername(request.username())) {
            throw new DuplicateUsernameException("이미 존재하는 아이디입니다: " + request.username());
        }
    }

    private void checkPasswordMatches(MemberCreateRequest request) {
        if (!request.isPasswordMatched()) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 회원가입 시 이름 중복 체크 (모든 이름에 대해 중복 체크)
     */
    private void checkDuplicateName(String name) {
        if (memberRepository.existsByName(name)) {
            throw new DuplicateNameException("이미 존재하는 사용자명입니다: " + name);
        }
    }

    /**
     * 회원정보 수정 시 이름 중복 체크 (본인의 현재 이름은 제외하고 중복 체크)
     */
    private void checkDuplicateName(String name, Member currentMember) {
        // 본인의 현재 이름과 같으면 중복 체크 패스

        if (currentMember.getName().equals(name)) {
            return;
        }

        // 다른 사람이 사용 중인 이름인지 체크
        if (memberRepository.existsByName(name)) {
            throw new DuplicateNameException("이미 존재하는 사용자명입니다: " + name);
        }
    }
}