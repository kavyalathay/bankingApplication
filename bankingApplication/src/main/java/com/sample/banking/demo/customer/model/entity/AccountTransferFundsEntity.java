package com.sample.banking.demo.customer.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTransferFundsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transferId;
	@DecimalMin(value = "1.0", message = "Value must be at least 1 dollar")
	private BigDecimal transactionAmount;
	@NonNull
	@CreationTimestamp
	private LocalDateTime transactionTimestamp;
	@NotBlank
	private String transactionName;
	@NonNull
	private Long fromAccountId;
	@NonNull
	private Long toAccountId;
	private Long fromAccountTransactionId;
	private Long toAccountTransactionId;
}
