package com.withus.withmebe.member.dto.auth;

public record SendAuthSmsResponseDto(
    int expirationSeconds,
    String authCode // TODO: test할 때만 편의상 리턴함.
) {}
