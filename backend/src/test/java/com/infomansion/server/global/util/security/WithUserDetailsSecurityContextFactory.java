package com.infomansion.server.global.util.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithUserDetails;

public class WithUserDetailsSecurityContextFactory implements WithSecurityContextFactory<WithCustomUserDetails> {

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public WithUserDetailsSecurityContextFactory(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public SecurityContext createSecurityContext(WithCustomUserDetails withUser) {
        String email = withUser.email();
        UserDetails principal = customUserDetailsService.loadUserByUsername(email);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
