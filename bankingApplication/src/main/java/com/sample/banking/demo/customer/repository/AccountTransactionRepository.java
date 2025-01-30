package com.sample.banking.demo.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.banking.demo.customer.model.entity.AccountTransactionEntity;

public interface AccountTransactionRepository extends JpaRepository<AccountTransactionEntity, Long> {

	List<AccountTransactionEntity> findByAccountId(long accountId);

}
