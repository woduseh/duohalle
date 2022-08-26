package com.hac.duohalle.domain.profile.dto.response;

import com.hac.duohalle.domain.account.entity.Account;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private final Long id;

    private final boolean emailVerified;

    private final String email;

    private final String nickname;

    private final LocalDateTime joinedAt;

    private final String bio;

    private final String profileUrl;

    private final String occupation;

    private final String imageUrl;


    public ProfileResponseDto(Account account) {
        this.id = account.getId();
        this.emailVerified = account.isEmailVerified();
        this.email = account.getEmail();
        this.nickname = account.getNickname();
        this.joinedAt = account.getJoinedAt();
        this.bio = account.getBio();
        this.profileUrl = account.getProfileUrl();
        this.occupation = account.getOccupation();
        this.imageUrl = account.getImageUrl();
    }
}
