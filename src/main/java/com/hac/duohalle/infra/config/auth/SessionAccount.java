package com.hac.duohalle.infra.config.auth;

import com.hac.duohalle.domain.account.entity.Account;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SessionAccount implements Serializable {

    private String email;
    private String nickname;
    private String password;
    private boolean emailVerified;
    private String profileUrl;
    private String occupation;
    private String imageUrl;
    private LocalDateTime emailCheckTokenGeneratedAt;

    public SessionAccount(Account account) {
        this.email = account.getEmail();
        this.nickname = account.getNickname();
        this.password = account.getPassword();
        this.emailVerified = account.isEmailVerified();
        this.profileUrl = account.getProfileUrl();
        this.occupation = account.getOccupation();
        this.imageUrl = account.getImageUrl();
        this.emailCheckTokenGeneratedAt = account.getEmailCheckTokenGeneratedAt();
    }

    public static SessionAccount of(Account account) {
        return new SessionAccount(account);
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }
}
