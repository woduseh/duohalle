package com.hac.duohalle.infra.config.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserAccountSecurityContextFactory.class)
public @interface WithMockUserAccount {

    String nickname() default "작두";

    String email() default "jackdu@fakeemail.com";

    String password() default "fakepassword";
}
