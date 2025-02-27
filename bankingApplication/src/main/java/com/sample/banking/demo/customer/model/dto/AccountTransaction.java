package com.sample.banking.demo.customer.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransaction {
	private Long transactionId;
	private BigDecimal transactionAmount;
	private LocalDateTime transactionTimestamp;
	@NotBlank
	private String transactionName;
	@NonNull
	private Long accountId;
}
