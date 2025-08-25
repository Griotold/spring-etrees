package com.example.spring_etrees.domain.member;

public class MemberFixture {

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

    public static MemberCreateRequest createRequest(String username, String password,
                                                    String passwordCheck, String name) {
        return new MemberCreateRequest(
                username,
                password,
                passwordCheck,
                name, null, null, null, null
        );
    }

    public static MemberCreateRequest createRequest(String username, String name) {
        return new MemberCreateRequest(
                username,
                "secret123",
                "secret123",
                name, null, null, null, null);
    }

    public static MemberCreateRequest createRequest() {
        return createRequest("etrees", "myname");
    }

    public static MemberInfoUpdateRequest createInfoUpdateRequest(String name) {
        return new MemberInfoUpdateRequest(name, "010-9876-5432", "98765", "수정할 주소", "수정할 회사");
    }

    public static MemberInfoUpdateRequest createInfoUpdateRequest() {
        return createInfoUpdateRequest("updating name");
    }


}
