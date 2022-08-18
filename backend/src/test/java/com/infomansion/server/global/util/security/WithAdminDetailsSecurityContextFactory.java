package com.infomansion.server.global.util.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithAdminDetailsSecurityContextFactory implements WithSecurityContextFactory<WithCustomAdminDetails> {

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public WithAdminDetailsSecurityContextFactory(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public SecurityContext createSecurityContext(WithCustomAdminDetails withUser) {
        String email = withUser.email();
        UserDetails principal = new User("1", "testPassword1$", Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
