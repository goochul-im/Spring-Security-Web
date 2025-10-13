package com.springsecurityjsw.domain.entity;

import com.springsecurityjsw.domain.dto.AccountDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private int age;
    private String roles;

    public AccountDto toDto() {
        return new AccountDto(
                id,
                username,
                password,
                age,
                roles
        );
    }
}
