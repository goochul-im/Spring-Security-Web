package com.springsecurityjsw.domain.dto;

public record AccountDto(
        String id,
        String username,
        String password,
        String age,
        String roles
) {
}
