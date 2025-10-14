package com.springsecurityjsw.security.provider;

import com.springsecurityjsw.domain.dto.AccountContext;
import com.springsecurityjsw.security.details.FormAuthenticationDetails;
import com.springsecurityjsw.security.details.FormWebAuthenticationDetailsSource;
import com.springsecurityjsw.security.exception.SecretException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext userDetails = (AccountContext)userDetailsService.loadUserByUsername(loginId);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("invalid password");
        }

        String secretKey = ((FormAuthenticationDetails) authentication.getDetails()).getSecretKey();
        if (secretKey == null || !secretKey.equals("secret")) {
            throw new SecretException("invalid secret");
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getAccountDto(), null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
