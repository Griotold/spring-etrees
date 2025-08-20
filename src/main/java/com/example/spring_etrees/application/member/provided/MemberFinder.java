package com.example.spring_etrees.application.member.provided;

import com.example.spring_etrees.domain.member.Member;

public interface MemberFinder {
    Member findByUsername(String username);
    boolean existsByUsername(String username);
}
