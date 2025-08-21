package com.example.spring_etrees.application.member;

import com.example.spring_etrees.application.member.provided.MemberRegister;
import com.example.spring_etrees.application.member.required.MemberRepository;
import com.example.spring_etrees.domain.member.*;
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
    public Member registerMember(MemberCreateRequest request) {
        validateMemberRegistration(request);

        Member member = Member.register(request, passwordEncoder);

        memberRepository.save(member);

        return member;
    }

    private void validateMemberRegistration(MemberCreateRequest request) {
        checkDuplicateUsername(request);
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
}