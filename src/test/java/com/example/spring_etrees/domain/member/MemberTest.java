package com.example.spring_etrees.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.spring_etrees.domain.member.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(member.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("secret123", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("invalid", passwordEncoder)).isFalse();
    }

    @Test
    void updateInfo() {
        MemberInfoUpdateRequest updateRequest = createInfoUpdateRequest();
        member.updateInfo(updateRequest);

        assertThat(member.getName()).isEqualTo("updating name");
        assertThat(member.getPhone()).isEqualTo("010-9876-5432");
    }

    @Test
    void promoteToAdmin() {
        assertThat(member.getRole()).isEqualTo(Role.USER);

        member.promoteToAdmin();

        assertThat(member.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void isAdmin() {
        assertThat(member.isAdmin()).isFalse();

        member.promoteToAdmin();

        assertThat(member.isAdmin()).isTrue();
    }
}