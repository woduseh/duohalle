package com.hac.duohalle.infra.config.auth;

import com.hac.duohalle.domain.account.entity.Account;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserAccount extends User {

    private final Account account;

    public UserAccount(Account account) {
        super(account.getEmail(), account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAccount that = (UserAccount) o;
        return Objects.equals(account, that.account);
    }
}
