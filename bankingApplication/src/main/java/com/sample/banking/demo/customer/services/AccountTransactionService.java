package com.sample.banking.demo.customer.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sample.banking.demo.customer.model.dto.AccountDetails;
import com.sample.banking.demo.customer.model.dto.AccountTransaction;
import com.sample.banking.demo.customer.model.dto.CustomerDetails;
import com.sample.banking.demo.customer.model.entity.AccountEntity;
import com.sample.banking.demo.customer.model.entity.CustomerEntity;
import com.sample.banking.demo.customer.model.mapper.AccountMapper;
import com.sample.banking.demo.customer.model.mapper.AccountTransactionMapper;
import com.sample.banking.demo.customer.model.mapper.CustomerMapper;
import com.sample.banking.demo.customer.repository.AccountRepository;
import com.sample.banking.demo.customer.repository.AccountTransactionRepository;
import com.sample.banking.demo.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final AccountTransactionRepository accountTransactionRepository;

	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private AccountTransactionMapper accountTransactionMapper;

	/**
	 * @param accountId
	 * @return
	 */
	public Optional<AccountDetails> getByAccountId(long accountId) {
		Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
		if (accountEntity.isPresent()) {
			AccountDetails.builder().account(accountMapper.toDto(accountEntity.get()))
					.transactions(
							accountTransactionMapper.toDto(accountTransactionRepository.findByAccountId(accountId)))
					.build();
		}
		return Optional.empty();
	}

	/**
	 * 
	 * @param customerId
	 * @return
	 */
	public Optional<CustomerDetails> getByCustomerId(long customerId) {
		Optional<CustomerEntity> customerEntity = customerRepository.findById(customerId);
		// customerDetails
		CustomerDetails cstomerDetails = CustomerDetails.builder().accountDetails(Collections.emptyList()).build();
		if (customerEntity.isPresent()) {
			cstomerDetails.setCustomer(customerMapper.toDto(customerEntity.get()));
			// all accounts
			List<AccountEntity> accountEntities = accountRepository.findByCustomerId(customerId);
			accountEntities.stream().forEach(accountEntity -> {
				// add account details
				AccountDetails accountDetails = AccountDetails.builder().account(accountMapper.toDto(accountEntity))
						.transactions(Collections.emptyList()).build();
				cstomerDetails.getAccountDetails().add(accountDetails);
				// transactions
				List<AccountTransaction> accountTransactions = accountTransactionMapper
						.toDto(accountTransactionRepository.findByAccountId(accountEntity.getAccountId()));
				if (CollectionUtils.isEmpty(accountTransactions)) {
					accountDetails.getTransactions().addAll(accountTransactionMapper.toDto(
							accountTransactionRepository.findByAccountId(accountDetails.getAccount().getAccountId())));
				}
			});
			return Optional.of(cstomerDetails);
		}
		return Optional.empty();
	}

}
