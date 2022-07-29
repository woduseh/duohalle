package com.hac.duohalle.infra.mail.service;

import com.hac.duohalle.infra.mail.dto.MailRequestDto;
import com.hac.duohalle.infra.mail.utils.MailHandler;
import java.io.IOException;
import javax.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JavaMailSender mailSender;

    @Value("${email.from.address}")
    private static String fromAddress;

    public void sendMail(MailRequestDto mailDto) throws MessagingException, IOException {
        MailHandler mailHandler = new MailHandler(mailSender);

        // 받는 사람
        mailHandler.setTo(mailDto.getAddress());
        // 보내는 사람
        mailHandler.setFrom(MailService.fromAddress);
        // 제목
        mailHandler.setSubject(mailDto.getTitle());
        // HTML Layout
        String htmlContent = "<p>" + mailDto.getMessage() + "<p> <img src='cid:sample-img'>";
        mailHandler.setText(htmlContent, true);
        // 첨부 파일
        mailHandler.setAttach("newTest.txt", "static/originTest.txt");
        // 이미지 삽입
        mailHandler.setInline("sample-img", "static/sample1.jpg");

        mailHandler.send();
    }
}
