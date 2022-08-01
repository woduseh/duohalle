package com.hac.duohalle.infra.mail.dto;

import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailRequestDto {

    // TODO:
    //  @Value("${site.domain}")
    //  private static String siteDomain;
    // 로 수정
    private static String siteDomain = "http://localhost:8080";

    private String address;
    private String title;
    private String message;

    @Override
    public String toString() {
        return "MailRequestDto{" +
                "address='" + address + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public static MailRequestDto signInConfirmMail(
            AccountSignUpRequestDto accountSignUpRequestDto) {
        return MailRequestDto.builder()
                .address(accountSignUpRequestDto.getEmail())
                .title("[Duohalle] 회원가입 인증")
                .message("안녕하세요, " + accountSignUpRequestDto.getNickname() + "님.\n"
                        + "Duohalle 회원가입을 환영합니다.\n"
                        + "아래 링크를 클릭하여 회원가입을 완료하세요.\n"
                        + siteDomain + "/sign-up/confirm/"
                        + accountSignUpRequestDto.getEmail() + "\n"
                        + "감사합니다.")
                .build();
    }
}
