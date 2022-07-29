package com.hac.duohalle.infra.mail.controller;

import com.hac.duohalle.infra.mail.dto.MailRequestDto;
import com.hac.duohalle.infra.mail.service.MailService;
import java.io.IOException;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MailService mailService;

    @PostMapping("/mail")
    public void mail(MailRequestDto mailDto) throws MessagingException, IOException {
        mailService.sendMail(mailDto);
    }
}
