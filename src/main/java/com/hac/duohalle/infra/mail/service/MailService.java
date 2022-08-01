package com.hac.duohalle.infra.mail.service;

import com.hac.duohalle.infra.mail.dto.MailRequestDto;
import com.hac.duohalle.infra.mail.utils.MailHandler;
import javax.mail.MessagingException;
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

    public void sendMail(MailRequestDto mailDto) throws MessagingException {
        logger.info("sending mail - mailDto: {}", mailDto);

        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            mailHandler.setTo(mailDto.getAddress());
            mailHandler.setSubject(mailDto.getTitle());
            String htmlContent = "<p>" + mailDto.getMessage() + "<p>";
            mailHandler.setText(htmlContent, true);

            mailHandler.send();
        } catch (Exception e) {
            logger.info("sending mail failure - mailDto: {}, error: {}", mailDto, e.getMessage());
        }
    }
}
