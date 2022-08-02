package com.hac.duohalle.domain.account.service;

import static com.hac.duohalle.infra.mail.dto.MailRequestDto.signInConfirmMail;

import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.repository.AccountRepository;
import com.hac.duohalle.infra.mail.service.MailService;
import java.util.Optional;
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

    @Transactional
    public Account signUp(AccountSignUpRequestDto form) {
        try {
            accountInfoDuplicateCheck(form.getEmail(), form.getNickname());
            accountRepository.save(form.toAccount());
            mailService.sendMail(signInConfirmMail(form));
        } catch (IllegalStateException e) {
            logger.info("sign-up failure - form: {}, error: {}", form, e.getMessage());
        }
        return null;
    }

    @Transactional
    public void confirm(String email) {
        Optional<Account> account = accountRepository.findAccountByEmail(email);

        account.ifPresentOrElse(acc -> {
                    acc.makeEmailVerified();
                    accountRepository.save(acc);
                    logger.info("Account Email {} confirmed", acc.getEmail());
                },
                () -> logger.info("Account Email {} not found", email));
    }

    private void accountInfoDuplicateCheck(String email, String nickname) {
        Optional<Account> accountFindByEmail = accountRepository.findAccountByEmail(email);
        if (accountFindByEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Optional<Account> accountFindByNickname = accountRepository.findAccountByNickname(nickname);
        if (accountFindByNickname.isPresent()) {
            throw new IllegalArgumentException("Nickname already exists");
        }
    }
}
