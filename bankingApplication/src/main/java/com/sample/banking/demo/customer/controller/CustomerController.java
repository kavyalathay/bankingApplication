package com.sample.banking.demo.customer.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.sample.banking.demo.customer.exception.BankingException;
import com.sample.banking.demo.customer.model.dto.Customer;
import com.sample.banking.demo.customer.services.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping("/id/{customerId}")
	private ResponseEntity<Customer> getByCustomerId(@PathVariable String customerId) {
		Optional<Customer> customer = customerService.getCustomerDetailsById(customerId);
		if (customer.isPresent()) {
			return ResponseEntity.ok(customer.get());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/id/{emailId}")
	private ResponseEntity<Customer> getByEmailId(@PathVariable String emailId) {
		Optional<Customer> customer = customerService.getCustomerDetailsByEmailId(emailId);
		if (customer.isPresent()) {
			return ResponseEntity.ok(customer.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/register")
	private ResponseEntity<Customer> createCustomer(@RequestBody @Valid Customer customer,
			UriComponentsBuilder uriBuilder) throws BankingException {
		Customer customerResponse = customerService.createCustomer(customer);
		return ResponseEntity
				.created(uriBuilder.path("/id/{customerId}").buildAndExpand(customer.getCustomerId()).toUri())
				.body(customerResponse);
	}

}
