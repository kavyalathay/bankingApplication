package com.sample.banking.demo.customer.model.entity;

import java.math.BigDecimal;

import com.sample.banking.demo.customer.enums.AccountType;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class AccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;
	private BigDecimal balance;
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	@Nonnull
	private Long customerId;
}
