package com.sample.banking.demo.customer.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountTransferFunds {
	private Long transferId;
	@DecimalMin(value = "1.0", message = "Value must be at least 1 dollar")
	private BigDecimal transactionAmount;
	private LocalDateTime transactionTimestamp;
	@NotBlank
	private String transactionName;
	@NonNull
	private Long fromAccountId;
	@NonNull
	private Long toAccountId;
}
