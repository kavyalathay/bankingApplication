package com.sample.banking.demo.customer.model.dto;

import java.math.BigDecimal;

import org.springframework.lang.NonNull;

import com.sample.banking.demo.customer.enums.AccountType;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Account {
	private Long accountId;
	@NonNull
	private AccountType accountType;
	@Nonnull
	private Long customerId;
	private BigDecimal accountBalance;
	@NotNull
	private AccountTransaction accountInitialTransaction;
	
}
