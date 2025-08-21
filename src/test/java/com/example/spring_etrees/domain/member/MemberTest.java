package com.example.spring_etrees.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.spring_etrees.domain.member.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = createPasswordEncoder();
        member = Member.register(createRequest(), passwordEncoder);
    }

    @Test
    void registerMember() {
        assertThat(member.getUsername()).isEqualTo("etrees");
        assertThat(member.getName()).isEqualTo("myname");
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("secret123", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("invalid", passwordEncoder)).isFalse();
    }
}