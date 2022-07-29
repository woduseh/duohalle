package com.hac.duohalle.domain.account.repository;

import static com.hac.duohalle.domain.account.entity.QAccount.account;

import com.hac.duohalle.domain.account.entity.Account;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public AccountCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(account)
                .where(account.email.eq(email))
                .fetchOne());
    }
}
