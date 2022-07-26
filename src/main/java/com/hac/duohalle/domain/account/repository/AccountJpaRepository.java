package com.hac.duohalle.domain.account.repository;

import com.hac.duohalle.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Account findByEmail(String email);

    Account findByNickname(String nickname);
}
