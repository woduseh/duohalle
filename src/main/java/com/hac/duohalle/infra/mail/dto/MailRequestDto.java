package com.hac.duohalle.infra.mail.dto;

import com.hac.duohalle.domain.account.form.SignUpForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailRequestDto {

    private String address;
    private String title;
    private String message;

    public static MailRequestDto signInConfirmMail(SignUpForm signUpForm) {
        return MailRequestDto.builder()
                .address(signUpForm.getEmail())
                .title("[Duohalle] 회원가입 인증")
                .message("안녕하세요, " + signUpForm.getNickname() + "님.\n" +
                        "Duohalle 회원가입을 환영합니다.\n" +
                        "아래 링크를 클릭하여 회원가입을 완료하세요.\n" +
                        "http://localhost:8080/sign-up/confirm?email=" + signUpForm.getEmail()
                        + "\n" +
                        "감사합니다.")
                .build();
    }
}
