package com.example.spring_etrees.application.member.required;

import com.example.spring_etrees.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * username으로 회원 조회
     */
    Optional<Member> findByUsername(String username);

    /**
     * username 중복 체크
     */
    boolean existsByUsername(String username);

    /**
     * name 중복 체크
     */
    boolean existsByName(String name);
}