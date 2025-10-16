package com.springsecurityjsw.users.controller;

import com.springsecurityjsw.domain.dto.AccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("user/signup")
    public String signup(){
        return "login/signup";
    }

    @GetMapping("/login")
    public String login() {
        return "/login/login";
    }

    @GetMapping("/api/login")
    public String restLogin() {
        return "rest/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "redirect:/login";
    }

    @GetMapping("/denied")
    public String accessDenied(
            @RequestParam(value = "exception", required = false)String exception,
            @AuthenticationPrincipal AccountDto accountDto,
            Model model){ // Principal은 UserDetails의 UserAccountDto로 지정해뒀음

        model.addAttribute("username", accountDto.username());
        model.addAttribute("exception", exception);

        return "login/denied";
    }

}
