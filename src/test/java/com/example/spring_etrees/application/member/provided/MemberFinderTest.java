package com.example.spring_etrees.application.member.provided;

import com.example.spring_etrees.domain.member.Member;
import com.example.spring_etrees.domain.member.MemberCreateRequest;
import com.example.spring_etrees.domain.member.MemberFixture;
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
record MemberFinderTest(MemberFinder memberFinder,
                        MemberRegister memberRegister,
                        EntityManager entityManager) {

    @Test
    void findByUsername() {
        Member member = memberRegister.registerMember(MemberFixture.createRequest());

        entityManager.flush();
        entityManager.clear();

        var founded = memberFinder.findByUsername(member.getUsername());
        assertThat(founded.getId()).isEqualTo(member.getId());
        assertThat(founded.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    void findByUsername_없는_유저네임으로_찾는경우() {
        assertThatThrownBy(() -> memberFinder.findByUsername("없는 유저네임"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void existsByUsername() {
        Member member = memberRegister.registerMember(MemberFixture.createRequest());

        entityManager.flush();
        entityManager.clear();

        boolean result1 = memberFinder.existsByUsername(member.getUsername());
        boolean result2 = memberFinder.existsByUsername("없는 유저네임");

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void findById() {
        Member member = memberRegister.registerMember(MemberFixture.createRequest());

        entityManager.flush();
        entityManager.clear();

        var founded = memberFinder.findById(member.getId());
        assertThat(founded.getId()).isEqualTo(member.getId());
        assertThat(founded.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    void findById_없는_memberId() {
        assertThatThrownBy(() -> memberFinder.findById(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}