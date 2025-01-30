package com.sample.banking.demo.customer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sample.banking.demo.customer.exception.BankingClientException;
import com.sample.banking.demo.customer.model.dto.Customer;
import com.sample.banking.demo.customer.model.entity.CustomerEntity;
import com.sample.banking.demo.customer.model.mapper.CustomerMapper;
import com.sample.banking.demo.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;
	@Autowired
	private CustomerMapper customerMapper;

	public Optional<Customer> getCustomerDetailsById(String customerId) {
		if (StringUtils.hasLength(customerId)) {
			Optional<CustomerEntity> customerEntity = customerRepository.findById(Long.valueOf(customerId));
			if (customerEntity.isPresent()) {
				return Optional.of(customerMapper.toDto(customerEntity.get()));
			}
		}
		return Optional.empty();
	}

	public Optional<Customer> getCustomerDetailsByEmailId(String emailId) {
		if (StringUtils.hasLength(emailId)) {
			Optional<CustomerEntity> customerEntity = customerRepository.findByEmail(emailId.trim());
			if (customerEntity.isPresent()) {
				return Optional.of(customerMapper.toDto(customerEntity.get()));
			}
		}
		return Optional.empty();
	}

	public Customer createCustomer(Customer customer) throws BankingClientException {
		List<String> validationErrors = validate(customer);
		if (!CollectionUtils.isEmpty(validationErrors)) {
			throw BankingClientException.builder().errorMessage(validationErrors.toString()).build();
		}
		return customerMapper.toDto(customerRepository.save(customerMapper.toEntity(customer)));
	}

	private List<String> validate(Customer customer) {
		List<String> errorMessages = new ArrayList<>();
		if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
			errorMessages.add("Email already used by another user");
		}
		if (customerRepository.findByUserName(customer.getUserName()).isPresent()) {
			errorMessages.add("User Name already taken");
		}
		return errorMessages;
	}

}
