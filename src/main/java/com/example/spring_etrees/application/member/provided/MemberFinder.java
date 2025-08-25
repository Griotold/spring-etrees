package com.example.spring_etrees.application.member.provided;

import com.example.spring_etrees.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberFinder {
    Member findByUsername(String username);

    boolean existsByUsername(String username);

    Member findById(Long id);

    Page<Member> findAll(Pageable pageable);
}
