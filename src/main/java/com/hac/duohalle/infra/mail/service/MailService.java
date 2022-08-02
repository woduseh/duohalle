package com.hac.duohalle.infra.mail.service;

import com.hac.duohalle.infra.mail.dto.MailRequestDto;
import com.hac.duohalle.infra.mail.utils.MailHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JavaMailSender mailSender;
//    private final TemplateEngine templateEngine;
//    private final AppProperties appProperties;

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
            logger.info("sending mail failure - mailDto: {}, error: {}", mailDto, e.getMessage());
        }
    }

//    public void sendSignUpConfirmEmail(AccountSignUpRequestDto dto) {
//        Context context = new Context();
//
//        context.setVariable("host", appProperties.getHost());
//        context.setVariable("link", "/check-email-token?token=" + newAccount.getEmailCheckToken() +
//                "&email=" + dto.getEmail());
//        context.setVariable("nickname", dto.getNickname());
//        context.setVariable("linkName", "이메일 인증하기");
//        String message = templateEngine.process("email/sign-up-confirm", context);
//
//        MailRequestDto mailDto = MailRequestDto.builder()
//                .address(dto.getEmail())
//                .title("[Duohalle] 가입 인증 메일")
//                .message(message)
//                .build();
//
//        sendMail(mailDto);
//    }
}
