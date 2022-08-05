package com.hac.duohalle.infra.config.auth;

import com.hac.duohalle.domain.account.entity.Account;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserAccountSecurityContextFactory implements
        WithSecurityContextFactory<WithMockUserAccount> {

    @Override
    public SecurityContext createSecurityContext(WithMockUserAccount customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserAccount account = new UserAccount(Account.builder()
                .email("jackdu@fakeemail.com")
                .password("fakepassword")
                .nickname("jackdu")
                .build());
        Authentication auth = new UsernamePasswordAuthenticationToken(account,
                account.getPassword(), account.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}