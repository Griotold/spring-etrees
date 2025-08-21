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
}
