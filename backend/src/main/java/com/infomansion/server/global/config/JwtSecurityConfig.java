package com.infomansion.server.global.config;

import com.infomansion.server.global.util.jwt.JwtFiler;
import com.infomansion.server.global.util.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider  tokenProvider;

    // Custom한 JwtFilter를 Spring Security Filter에 등록합니다.
    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JwtFiler jwtFiler = new JwtFiler(tokenProvider);
        builder.addFilterBefore(jwtFiler, UsernamePasswordAuthenticationFilter.class);
    }
}
