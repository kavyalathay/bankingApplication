package com.sample.banking.demo.customer.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.banking.demo.customer.model.dto.AccountDetails;
import com.sample.banking.demo.customer.model.dto.CustomerDetails;
import com.sample.banking.demo.customer.services.AccountTransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
	private final AccountTransactionService accountTransactionService;

	@GetMapping("/{accountId}")
	private ResponseEntity<AccountDetails> getAccountTransactions(@PathVariable long accountId) {
		Optional<AccountDetails> accountDetails = accountTransactionService.getByAccountId(accountId);
		if (accountDetails.isPresent()) {
			return ResponseEntity.ok(accountDetails.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{customerId}")
	private ResponseEntity<CustomerDetails> getCustomerTransactions(@PathVariable long customerId) {
		Optional<CustomerDetails> customerDetails = accountTransactionService.getByCustomerId(customerId);
		if (customerDetails.isPresent()) {
			return ResponseEntity.ok(customerDetails.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
