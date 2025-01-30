package com.sample.banking.demo.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.banking.demo.customer.model.dto.Customer;
import com.sample.banking.demo.customer.model.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	Optional<CustomerEntity> findByEmail(String email);

	Optional<CustomerEntity> findByUserName(String userName);
}
