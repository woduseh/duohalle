package com.hac.duohalle.domain.account.service;

import com.hac.duohalle.domain.account.dto.request.AccountConfirmRequestDto;
import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.repository.AccountRepository;
import com.hac.duohalle.infra.config.auth.SessionAccount;
import com.hac.duohalle.infra.config.auth.UserAccount;
import com.hac.duohalle.infra.mail.service.MailService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HttpSession httpSession;
    private final AccountRepository accountRepository;
    private final MailService mailService;

    @Transactional
    public Account signUp(AccountSignUpRequestDto dto) {
        try {
            Account newAccount = saveNewAccount(dto);
            sendConfirmEmail(newAccount);

            return newAccount;
        } catch (IllegalStateException e) {
            logger.warn("Sign-up failure - form: {}, Error: {}", dto, e.getMessage());
        }
        return new Account();
    }

    private Account saveNewAccount(AccountSignUpRequestDto dto) {
        Account account = dto.toAccount();
        account.generateEmailCheckToken();
        return accountRepository.save(account);
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
                        new UserAccount(account), account.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        httpSession.setAttribute("account", SessionAccount.of(account));
    }

    @Transactional
    public void resendConfirmEmail(String email) {
        Optional<Account> account = accountRepository.findAccountByEmail(email);
        account.ifPresentOrElse(
                this::sendConfirmEmail, () -> logger.warn("Account not found - Email: {} ", email));
    }

    private void sendConfirmEmail(Account account) {
        try {
            checkConfirmEmailCanSend(account);
            mailService.sendSignUpConfirmEmail(account);
            account.generateEmailCheckToken();
        } catch (IllegalStateException e) {
            logger.warn("Account confirm fail - Email: {}, Error: {}", account.getEmail(),
                    e.getMessage());
        }
    }

    private void checkConfirmEmailCanSend(Account account) throws IllegalStateException {
        if (account.isEmailVerified()) {
            throw new IllegalStateException("Email is already verified");
        }

        if (!account.canSendConfirmEmail()) {
            throw new IllegalStateException("Account confirm mail can be sent only once per hour");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Account not found - Email: " + username));

        httpSession.setAttribute("account", SessionAccount.of(account));

        return new UserAccount(account);
    }
}
