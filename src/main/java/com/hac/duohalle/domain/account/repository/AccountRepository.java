package com.hac.duohalle.domain.account.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends AccountCustomRepository, AccountJpaRepository {

}
