package com.sample.banking.demo.customer.model.dto;

import java.math.BigDecimal;

import org.springframework.lang.NonNull;

import com.sample.banking.demo.customer.enums.AccountType;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Account {
	private Long accountId;
	@DecimalMin(value = "10.0", message = "Value must be at least 10 dollars")
	private BigDecimal balance;
	@NonNull
	private AccountType accountType;
	@Nonnull
	private Long customerId;
}
