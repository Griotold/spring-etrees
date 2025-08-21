package com.example.spring_etrees.application.member.provided;

import com.example.spring_etrees.domain.member.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register() {
        MemberCreateRequest request = MemberFixture.createRequest();
        Member member = memberRegister.registerMember(request);

        assertThat(member.getId()).isNotNull();
    }

    @Test
    void duplicateUsernameFail() {
        memberRegister.registerMember(MemberFixture.createRequest());

        assertThatThrownBy(() -> memberRegister.registerMember(MemberFixture.createRequest()))
                .isInstanceOf(DuplicateUsernameException.class);
    }

    @Test
    void passwordNotMatchesFail() {
        var request = MemberFixture.createRequest("etrees", "password", "invalid", "myname");

        assertThatThrownBy(() -> memberRegister.registerMember(request)).isInstanceOf(PasswordMismatchException.class);
    }
}