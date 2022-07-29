package com.hac.duohalle.domain.account.service;

import static com.hac.duohalle.infra.mail.dto.MailRequestDto.signInConfirmMail;

import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.form.SignUpForm;
import com.hac.duohalle.domain.account.repository.AccountRepository;
import com.hac.duohalle.infra.mail.service.MailService;
import java.io.IOException;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountRepository accountRepository;
    private final MailService mailService;

    public Account signUp(SignUpForm form) throws MessagingException, IOException {
        mailService.sendMail(signInConfirmMail(form));
        return accountRepository.save(form.toAccount());
    }

    @Transactional
    public void confirm(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);

        account.ifPresentOrElse(acc -> {
                    acc.makeEmailVerified();
                    accountRepository.save(acc);
                    logger.info("Account Email {} confirmed", acc.getEmail());
                },
                () -> logger.info("Account Email {} not found", email));
    }
}
