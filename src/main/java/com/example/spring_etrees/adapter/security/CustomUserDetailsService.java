package com.example.spring_etrees.adapter.security;

import com.example.spring_etrees.application.member.provided.MemberFinder;
import com.example.spring_etrees.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberFinder memberFinder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Member member = memberFinder.findByUsername(username);
            return new LoginUser(member);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username, e);
        }
    }
}