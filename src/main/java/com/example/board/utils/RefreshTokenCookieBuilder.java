package com.example.board.utils;

import org.springframework.http.ResponseCookie;

public class RefreshTokenCookieBuilder {
    private static final Long MAX_AGE = 24 * 60 * 60 * 1000L;

    public static ResponseCookie build(String refreshToken) {
        return ResponseCookie
                .from("Refresh-Token", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(MAX_AGE)
                .build();
    }

    public static ResponseCookie buildRemovedCookie() {
        return ResponseCookie
                .from("Refresh-Token", "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();
    }
}