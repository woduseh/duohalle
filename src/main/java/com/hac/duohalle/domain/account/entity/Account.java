package com.hac.duohalle.domain.account.entity;

import com.hac.duohalle.domain.global.entity.BaseTimeEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true)
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Column(unique = true, length = 20)
    @Length(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
    private String nickname;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상 20자 이하여야 합니다.")
    @Column(length = 20)
    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    @Comment("가입일")
    private LocalDateTime joinedAt;

    @Comment("자기소개")
    private String bio;

    @Comment("사용자 프로필 URL")
    @URL(message = "사용자 프로필 URL이 올바르지 않습니다.")
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileUrl;

    @Comment("직업")
    private String occupation;

    @Comment("사용자 이미지 URL")
    @URL(message = "사용자 이미지 URL이 올바르지 않습니다.")
    private String imageUrl;

    @Comment("계정 삭제 여부")
    private boolean isDeleted;

    @Comment("웹 푸시 사용 여부")
    private boolean webPushEnabled;

    @Comment("파티 생성 알림 이메일 사용 여부")
    private boolean partyCreatedNoticeByEmail;

    @Comment("파티 생성 알림 웹 알림 사용 여부")
    private boolean partyCreatedNoticeByWeb;

    @Comment("파티 생성 알림 웹 푸시 사용 여부")
    private boolean partyCreatedNoticeByWebPush;

    @Comment("파티 참가 알림 이메일 사용 여부")
    private boolean partyEnrollmentNoticeByEmail;

    @Comment("파티 참가 알림 웹 알림 사용 여부")
    private boolean partyEnrollmentNoticeByWeb;

    @Comment("파티 참가 알림 웹 푸시 사용 여부")
    private boolean partyEnrollmentNoticeByWebPush;

    @Comment("파티 갱신 알림 이메일 사용 여부")
    private boolean partyUpdatedNoticeByEmail;

    @Comment("파티 갱신 알림 웹 알림 사용 여부")
    private boolean partyUpdatedNoticeByWeb;

    @Comment("파티 갱신 알림 웹 푸시 사용 여부")
    private boolean partyUpdatedNoticeByWebPush;

    private boolean isEmailVerified() {
        return emailVerified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
                o)) {
            return false;
        }
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
