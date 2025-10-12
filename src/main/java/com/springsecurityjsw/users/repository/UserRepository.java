package com.springsecurityjsw.users.repository;

import com.springsecurityjsw.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
}
