package com.springsecurityjsw.users.controller;

import com.springsecurityjsw.domain.dto.AccountDto;
import com.springsecurityjsw.domain.entity.Account;
import com.springsecurityjsw.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder encoder;
    private final UserService userService;

    @PostMapping("/signup")
    public String signup(AccountDto accountDto){

        Account account = accountDto.toEntity();
        account.setPassword(encoder.encode(accountDto.password()));
        userService.createUser(account);

        return "redirect:/";
    }

}
