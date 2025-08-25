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
        Member member = memberRegister.registerMember(MemberFixture.createRequest());

        assertThat(member.getId()).isNotNull();
    }

    @Test
    void duplicateUsernameFail() {
        memberRegister.registerMember(MemberFixture.createRequest());

        assertThatThrownBy(() -> memberRegister.registerMember(MemberFixture.createRequest()))
                .isInstanceOf(DuplicateUsernameException.class);
    }

    @Test
    void register_duplicateNameFail() {
        Member member = memberRegister.registerMember(MemberFixture.createRequest("etrees", "myname"));

        assertThatThrownBy(() -> memberRegister.registerMember(MemberFixture.createRequest("griotold", "myname")))
                .isInstanceOf(DuplicateNameException.class);
    }

    @Test
    void passwordNotMatchesFail() {
        var request = MemberFixture.createRequest("etrees", "password", "invalid", "myname");

        assertThatThrownBy(() -> memberRegister.registerMember(request)).isInstanceOf(PasswordMismatchException.class);
    }

    @Test
    void updateInfoMember() {
        Member member = memberRegister.registerMember(MemberFixture.createRequest());

        MemberInfoUpdateRequest updateRequest = MemberFixture.createInfoUpdateRequest();

        Member updated = memberRegister.updateInfoMember(updateRequest, member.getId());

        assertThat(updated.getId()).isEqualTo(member.getId());
        assertThat(updated.getName()).isEqualTo("updating name");
    }

    @Test
    void updateInfoMember_없는_회원일_때() {
        MemberInfoUpdateRequest updateRequest = MemberFixture.createInfoUpdateRequest();

        assertThatThrownBy(() -> memberRegister.updateInfoMember(updateRequest, 999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateInfoMember_중복된_name() {
        Member member1 = memberRegister.registerMember(MemberFixture.createRequest("etrees", "이트리즈"));
        Member member2 = memberRegister.registerMember(MemberFixture.createRequest("griotold", "고리오영감"));

        MemberInfoUpdateRequest updateRequest = MemberFixture.createInfoUpdateRequest("이트리즈");

        assertThatThrownBy(() -> memberRegister.updateInfoMember(updateRequest, member2.getId()))
                .isInstanceOf(DuplicateNameException.class);

    }

    @Test
    void promoteToAdmin() {
        Member member = memberRegister.registerMember(MemberFixture.createRequest());

        assertThat(member.isAdmin()).isFalse(); // 초기 상태는 일반 회원

        Member promoted = memberRegister.promoteToAdmin(member.getId());

        assertThat(promoted.getId()).isEqualTo(member.getId());
        assertThat(promoted.isAdmin()).isTrue();
        assertThat(promoted.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void promoteToAdmin_없는_회원일_때() {
        assertThatThrownBy(() -> memberRegister.promoteToAdmin(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void demoteToUser() {
        Member member = memberRegister.registerMember(MemberFixture.createRequest());

        // 먼저 관리자로 승격
        memberRegister.promoteToAdmin(member.getId());
        assertThat(member.isAdmin()).isTrue();

        // 일반회원으로 강등
        Member demoted = memberRegister.demoteToUser(member.getId());

        assertThat(demoted.getId()).isEqualTo(member.getId());
        assertThat(demoted.isAdmin()).isFalse();
        assertThat(demoted.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void demoteToUser_없는_회원일_때() {
        assertThatThrownBy(() -> memberRegister.demoteToUser(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}