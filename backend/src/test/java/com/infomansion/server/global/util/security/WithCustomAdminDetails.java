package com.infomansion.server.global.util.security;

import com.infomansion.server.domain.user.domain.UserAuthority;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(setupBefore = TestExecutionEvent.TEST_EXECUTION, factory = WithAdminDetailsSecurityContextFactory.class)
public @interface WithCustomAdminDetails {

    String email() default "admin@test.com";
    String password() default "testPassword1$";
    UserAuthority authority() default UserAuthority.ROLE_ADMIN;
}
