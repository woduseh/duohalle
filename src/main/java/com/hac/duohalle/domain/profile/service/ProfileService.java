package com.hac.duohalle.domain.profile.service;

import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AccountRepository accountRepository;

    public Account findByEmail(String email) {
        return accountRepository.findAccountByEmail(email).orElseThrow(
                () -> new IllegalStateException("Account not found - Email: " + email));
    }
}
