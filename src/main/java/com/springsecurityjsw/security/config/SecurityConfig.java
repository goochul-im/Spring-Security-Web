package com.springsecurityjsw.security.config;

import com.springsecurityjsw.security.details.FormWebAuthenticationDetailsSource;
import com.springsecurityjsw.security.dsl.RestApiDsl;
import com.springsecurityjsw.security.entrypoint.RestAuthenticationEntryPoint;
import com.springsecurityjsw.security.filter.RestAuthenticationFilter;
import com.springsecurityjsw.security.handler.FormAccessDeniedHandler;
import com.springsecurityjsw.security.handler.RestAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider formAuthenticationProvider;
    private final AuthenticationProvider restAuthenticationProvider;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationSuccessHandler restAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationFailureHandler restAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
                        .requestMatchers("/user").hasAuthority("ROLE_USER")
                        .requestMatchers("/manager").hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/", "/signup", "/user/signup", "/login*").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .authenticationDetailsSource(authenticationDetailsSource)
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .authenticationProvider(formAuthenticationProvider)
                .exceptionHandling(exception -> exception.accessDeniedHandler(new FormAccessDeniedHandler("/denied")));

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = managerBuilder.build();

        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
                        .requestMatchers("/api", "/api/login").permitAll()
                        .requestMatchers("/api/user").hasAuthority("ROLE_USER")
                        .requestMatchers("/api/manager").hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/api/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler()))
                .with(new RestApiDsl<>(), restDsl -> restDsl
                        .restSuccessHandler(restAuthenticationSuccessHandler)
                        .restFailureHandler(restAuthenticationFailureHandler)
                        .loginPage("/api/login")
                        .loginProcessingUrl("/api/login"))
        ;

        return http.build();
    }


}
