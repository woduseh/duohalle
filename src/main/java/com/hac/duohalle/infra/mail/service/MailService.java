package com.hac.duohalle.infra.mail.service;

import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.infra.config.AppProperties;
import com.hac.duohalle.infra.mail.dto.MailRequestDto;
import com.hac.duohalle.infra.mail.utils.MailHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    public void sendMail(MailRequestDto mailDto) {
        logger.info("sending mail - mailDto: {}", mailDto);

        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            mailHandler.setTo(mailDto.getAddress());
            mailHandler.setSubject(mailDto.getTitle());
            String htmlContent = mailDto.getMessage();
            mailHandler.setText(htmlContent, true);

            mailHandler.send();
        } catch (Exception e) {
            logger.warn("sending mail failure - mailDto: {}, Error: {}", mailDto, e.getMessage());
        }
    }

    public MailRequestDto sendSignUpConfirmEmail(Account newAccount) {
        Context context = new Context();

        context.setVariable("host", appProperties.getHost());
        context.setVariable("link",
                "/sign-up/check-email-token?token=" + newAccount.getEmailCheckToken() +
                        "&email=" + newAccount.getEmail());
        context.setVariable("nickname", newAccount.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        String message = templateEngine.process("mail/confirm-mail", context);

        MailRequestDto mailDto = MailRequestDto.builder()
                .address(newAccount.getEmail())
                .title("[Duohalle] 가입 인증 메일")
                .message(message)
                .build();

        sendMail(mailDto);

        return mailDto;
    }
}
