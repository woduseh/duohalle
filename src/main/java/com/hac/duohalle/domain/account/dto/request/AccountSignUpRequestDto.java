package com.hac.duohalle.domain.account.dto.request;

import static com.hac.duohalle.infra.config.WebSecurityConfig.getPasswordEncoder;

import com.hac.duohalle.domain.account.entity.Account;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AccountSignUpRequestDto {
    @Column(unique = true)
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @Column(unique = true, length = 20)
    @Length(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
    private String nickname;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상 20자 이하여야 합니다.")
    @Column(length = 20)
    private String password;

    public Account toAccount() {
        return Account.builder()
                .email(email)
                .nickname(nickname)
                .password(getPasswordEncoder().encode(password))
                .build();
    }
}
