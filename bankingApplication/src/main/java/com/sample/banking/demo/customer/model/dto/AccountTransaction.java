package com.sample.banking.demo.customer.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountTransaction {
	private BigDecimal transactionId;
	@NonNull
	private BigDecimal transactionAmount;
	@NonNull
	private LocalDateTime transactionTimestamp;
	@NotBlank
	private String transactionName;
	@NonNull
	private BigDecimal accountId;
}
