package com.hac.duohalle.domain.account.service;

import com.hac.duohalle.domain.account.dto.request.AccountConfirmRequestDto;
import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.repository.AccountRepository;
import com.hac.duohalle.infra.mail.service.MailService;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountRepository accountRepository;
    private final MailService mailService;

    @Transactional
    public Account signUp(AccountSignUpRequestDto dto) {
        try {
            accountInfoDuplicateCheck(dto);
            Account newAccount = saveNewAccount(dto);
            mailService.sendSignUpConfirmEmail(newAccount);

            return newAccount;
        } catch (IllegalStateException e) {
            logger.warn("sign-up failure - form: {}, Error: {}", dto, e.getMessage());
        }
        return new Account();
    }

    private Account saveNewAccount(AccountSignUpRequestDto dto) {
        Account account = dto.toAccount();
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }

    private void accountInfoDuplicateCheck(AccountSignUpRequestDto dto) {
        Optional<Account> accountFindByEmail = accountRepository.findAccountByEmail(dto.getEmail());
        if (accountFindByEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Optional<Account> accountFindByNickname = accountRepository.findAccountByNickname(
                dto.getNickname());
        if (accountFindByNickname.isPresent()) {
            throw new IllegalArgumentException("Nickname already exists");
        }
    }

    @Transactional
    public Account confirm(AccountConfirmRequestDto dto) {
        Optional<Account> account = accountRepository.findAccountByEmail(dto.getEmail());

        account.ifPresentOrElse(acc -> makeEmailVerified(acc, dto),
                () -> logger.warn("Account not found - Email: {} ", dto.getEmail()));

        return account.orElse(new Account());
    }

    private void makeEmailVerified(Account account, AccountConfirmRequestDto dto) {
        try {
            if (account.getEmailCheckToken().equals(dto.getToken())) {
                account.makeEmailVerified();
                accountRepository.save(account);
                logger.info("Account confirmed - Email: {} ", account.getEmail());
            } else {
                throw new IllegalStateException("Email check token is not matched");
            }
        } catch (IllegalStateException e) {
            logger.warn("Account confirm fail - Email: {}, Error: {}", account.getEmail(),
                    e.getMessage());
        }
    }

    public void login(Account account) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        account.getNickname(), account.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    }
}
