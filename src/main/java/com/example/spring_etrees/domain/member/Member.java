package com.example.spring_etrees.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD_HASH", length = 100, nullable = false)
    private String passwordHash;

    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Column(name = "POST_NO", length = 10)
    private String postNo;

    @Column(name = "ADDRESS", length = 200)
    private String address;

    @Column(name = "COMPANY", length = 100)
    private String company;

    @CreatedDate
    @Column(name = "CREATE_TIME", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "MODIFIED_TIME")
    private LocalDateTime modifiedTime;

    /**
     * 회원 등록 (정적 팩토리 메서드)
     */
    public static Member register(MemberCreateRequest registerRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.username = requireNonNull(registerRequest.username());
        member.passwordHash = requireNonNull(passwordEncoder.encode(registerRequest.password()));
        member.name = requireNonNull(registerRequest.name());
        member.phone = registerRequest.phone();
        member.postNo = registerRequest.postNo();
        member.address = registerRequest.address();
        member.company = registerRequest.company();

        return member;
    }

    /**
     * 비밀번호 검증
     */
    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, passwordHash);
    }
}
