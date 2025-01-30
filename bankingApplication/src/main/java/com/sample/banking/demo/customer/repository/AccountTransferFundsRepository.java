package com.sample.banking.demo.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.banking.demo.customer.model.entity.AccountTransferFundsEntity;

public interface AccountTransferFundsRepository extends JpaRepository<AccountTransferFundsEntity, Long> {

}
