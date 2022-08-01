package com.hac.duohalle.infra.mail.service;

import static com.hac.duohalle.infra.mail.dto.MailRequestDto.signInConfirmMail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import com.hac.duohalle.infra.mail.dto.MailRequestDto;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ErrorCollector;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSender;

class MailServiceTest {

    private final String mailFrom = "jackdu";

    private JavaMailSender mailSender;
    private MailRequestDto mailRequestDto;
    private MailService mailService;

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @BeforeEach
    public void setup() {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        mailSender = mock(JavaMailSender.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        mailService = new MailService(mailSender);
    }

    @DisplayName("회원 인증 메일 발송")
    @Test
    void sendSignUpMail() throws MessagingException {
        // given
        initMailDto();
        mailService.sendMail(mailRequestDto);

        // when
        ArgumentCaptor<MimeMessage> emailCaptor =
                ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(1)).send(emailCaptor.capture());

        // then
        List<MimeMessage> actualList = emailCaptor.getAllValues();
        assertThat(actualList.size()).isEqualTo(1);
        assertThat(actualList.get(0).getSubject()).isEqualTo(mailRequestDto.getTitle());
    }

    private void initMailDto() {
        AccountSignUpRequestDto accountSignUpRequestDto = new AccountSignUpRequestDto();
        accountSignUpRequestDto.setEmail("woduseh@humuson.com");
        accountSignUpRequestDto.setNickname("jackdu0703");

        mailRequestDto = signInConfirmMail(accountSignUpRequestDto);
    }
}