package com.hac.duohalle.domain.profile.service;

import static com.hac.duohalle.infra.config.WebSecurityConfig.getPasswordEncoder;

import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.repository.AccountRepository;
import com.hac.duohalle.domain.account.service.AccountService;
import com.hac.duohalle.domain.profile.dto.request.ProfileChangeRequestDto;
import com.hac.duohalle.domain.profile.dto.response.ProfileResponseDto;
import com.hac.duohalle.infra.config.auth.SessionAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ProfileResponseDto findByEmail(String email) {
        Account account = accountRepository.findAccountByEmail(email).orElseThrow(
                () -> new IllegalStateException("Account not found - Email: " + email));

        return new ProfileResponseDto(account);
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto findByNickname(String nickname) {
        Account account = accountRepository.findAccountByNickname(nickname).orElseThrow(
                () -> new IllegalStateException("Account not found - Nickname: " + nickname));

        return new ProfileResponseDto(account);
    }

    @Transactional(timeout = 10, rollbackFor = Exception.class)
    public void updateProfile(SessionAccount sessionAccount, ProfileChangeRequestDto dto) {
        Account account = accountRepository.findAccountByEmail(sessionAccount.getEmail())
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Account not found - Email: " + sessionAccount.getEmail()));
        account.updateProfile(dto);
    }

    @Transactional(timeout = 10, rollbackFor = Exception.class)
    public void updatePassword(SessionAccount sessionAccount, String newPassword) {
        Account account = accountRepository.findAccountByEmail(sessionAccount.getEmail())
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Account not found - Email: " + sessionAccount.getEmail()));
        account.updatePassword(getPasswordEncoder().encode(newPassword));
    }

    @Transactional(timeout = 10, rollbackFor = Exception.class)
    public void updateNickname(SessionAccount sessionAccount, String nickname) {
        Account account = accountRepository.findAccountByEmail(sessionAccount.getEmail())
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Account not found - Email: " + sessionAccount.getEmail()));
        account.updateNickname(nickname);
        accountService.login(account);
    }
}
