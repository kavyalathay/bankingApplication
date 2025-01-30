package com.sample.banking.demo.customer.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sample.banking.demo.customer.exception.BankingException;
import com.sample.banking.demo.customer.model.dto.Account;
import com.sample.banking.demo.customer.model.dto.AccountDetails;
import com.sample.banking.demo.customer.model.dto.CustomerDetails;
import com.sample.banking.demo.customer.model.entity.AccountEntity;
import com.sample.banking.demo.customer.model.mapper.AccountMapper;
import com.sample.banking.demo.customer.repository.AccountRepository;
import com.sample.banking.demo.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	@Autowired
	private AccountMapper accountMapper;

	/**
	 * 
	 * @param accountId
	 * @return
	 */
	public Optional<Account> getByAccountId(Long accountId) {
		if (accountId != null) {
			Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
			if (accountEntity.isPresent()) {
				return Optional.of(accountMapper.toDto(accountEntity.get()));
			}
		}
		return Optional.empty();
	}

	/**
	 * 
	 * @param customerId
	 * @return
	 */
	public Optional<CustomerDetails> getAccountsByCustomerId(Long customerId) {
		if (customerId != null) {
			List<AccountEntity> accountEntities = accountRepository.findByCustomerId(customerId);
			CustomerDetails customerDetails = CustomerDetails.builder().accountDetails(Collections.emptyList()).build();
			if (!CollectionUtils.isEmpty(accountEntities)) {
				accountEntities.stream().forEach(accountEntity -> {
					customerDetails.getAccountDetails()
							.add(AccountDetails.builder().account(accountMapper.toDto(accountEntity)).build());
				});
				return Optional.of(customerDetails);
			}
		}
		return Optional.empty();
	}

	/**
	 * 
	 * @param account
	 * @return
	 * @throws BankingException
	 */
	public Account createAccount(Account account) throws BankingException {
		if (customerRepository.findById(account.getCustomerId()).isPresent()) {
			AccountEntity accountEntity = accountMapper.toEntity(account);
			accountEntity = accountRepository.save(accountEntity);
			return accountMapper.toDto(accountEntity);
		} else {
			throw BankingException.builder().errorMessage("Incorrect Customer Details " + account.getCustomerId())
					.build();
		}

	}

}
