package com.sample.banking.demo.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.banking.demo.customer.model.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
//	Optional<AccountEntity> findByCustomer(CustomerEntity customer);
	List<AccountEntity> findByCustomerId(Long customerId);
}
