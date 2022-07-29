package com.hac.duohalle.domain.account.repository;

import com.hac.duohalle.domain.account.entity.Account;
import java.util.Optional;

public interface AccountCustomRepository {

    Optional<Account> findByEmail(String email);
}
