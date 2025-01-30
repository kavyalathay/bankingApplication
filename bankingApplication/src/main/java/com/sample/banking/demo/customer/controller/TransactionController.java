package com.sample.banking.demo.customer.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.banking.demo.customer.exception.BankingClientException;
import com.sample.banking.demo.customer.model.dto.AccountDetails;
import com.sample.banking.demo.customer.model.dto.AccountTransaction;
import com.sample.banking.demo.customer.model.dto.AccountTransferFunds;
import com.sample.banking.demo.customer.model.dto.CustomerDetails;
import com.sample.banking.demo.customer.services.AccountTransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
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

	@PostMapping("/post")
	private ResponseEntity<AccountTransaction> createTransation(@RequestBody AccountTransaction accountTransaction)
			throws BankingClientException {
		Optional<AccountTransaction> accountTransactionResponse = accountTransactionService
				.createTransaction(accountTransaction);
		if (accountTransactionResponse.isPresent()) {
			return ResponseEntity.ok(accountTransactionResponse.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/transfer")
	private ResponseEntity<AccountTransferFunds> createFundsTransfer(
			@RequestBody AccountTransferFunds accountTransferFunds) throws BankingClientException {
		AccountTransferFunds transferFundsResponse = accountTransactionService
				.createFundsTransfer(accountTransferFunds);
		if (transferFundsResponse != null) {
			return ResponseEntity.ok(transferFundsResponse);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
