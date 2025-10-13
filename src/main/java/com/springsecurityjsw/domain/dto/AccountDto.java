package com.springsecurityjsw.domain.dto;

import com.springsecurityjsw.domain.entity.Account;

public record AccountDto(
        Long id,
        String username,
        String password,
        int age,
        String roles
) {

    public Account toEntity() {
        return new Account(
                null,
                username,
                password,
                age,
                roles
        );
    }

}
