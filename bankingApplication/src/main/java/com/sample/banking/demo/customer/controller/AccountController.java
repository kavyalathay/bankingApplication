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
import org.springframework.web.util.UriComponentsBuilder;

import com.sample.banking.demo.customer.exception.BankingException;
import com.sample.banking.demo.customer.model.dto.Account;
import com.sample.banking.demo.customer.model.dto.CustomerDetails;
import com.sample.banking.demo.customer.services.AccountService;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
	private final AccountService accountService;

	@PostMapping("/create")
	public ResponseEntity<Account> create(@RequestBody @Validated Account account, UriComponentsBuilder uriBuilder)
			throws BankingException {
		Account accountResponse = accountService.createAccount(account);
		return ResponseEntity.created(uriBuilder.path("/id/{accountId}").buildAndExpand(account.getAccountId()).toUri())
				.body(accountResponse);
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<Account> getAccountDetails(@PathVariable @Nonnull Long accountId,
			UriComponentsBuilder uriBuilder) {
		Optional<Account> account = accountService.getByAccountId(accountId);
		if (account.isPresent()) {
			return ResponseEntity.ok(account.get());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerDetails> getCustomerAccounts(@PathVariable @Nonnull Long accountId) {
		Optional<CustomerDetails> customerDetails = accountService.getAccountsByCustomerId(accountId);
		if (customerDetails.isPresent()) {
			return ResponseEntity.ok(customerDetails.get());
		}
		return ResponseEntity.notFound().build();
	}

}
