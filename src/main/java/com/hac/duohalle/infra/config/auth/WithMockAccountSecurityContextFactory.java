package com.hac.duohalle.infra.config.auth;

import com.hac.duohalle.domain.account.entity.Account;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithMockAccountSecurityContextFactory implements
        WithSecurityContextFactory<WithMockAccount> {

    private final HttpSession httpSession;

    @Override
    public SecurityContext createSecurityContext(WithMockAccount customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Account account = Account.builder()
                .email("jackdu@fakeemail.com")
                .password("fakepassword")
                .nickname("jackdu")
                .emailCheckTokenGeneratedAt(LocalDateTime.now())
                .build();

        UserAccount userAccount = new UserAccount(account);
        Authentication auth = new UsernamePasswordAuthenticationToken(userAccount,
                userAccount.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        context.setAuthentication(auth);
        httpSession.setAttribute("account", SessionAccount.of(account));
        return context;
    }
}