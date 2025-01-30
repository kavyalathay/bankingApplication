package com.sample.banking.demo.customer.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sample.banking.demo.customer.exception.BankingClientException;
import com.sample.banking.demo.customer.model.dto.Account;
import com.sample.banking.demo.customer.model.dto.AccountDetails;
import com.sample.banking.demo.customer.model.dto.CustomerDetails;
import com.sample.banking.demo.customer.model.entity.AccountEntity;
import com.sample.banking.demo.customer.model.mapper.AccountMapper;
import com.sample.banking.demo.customer.model.mapper.AccountTransactionMapper;
import com.sample.banking.demo.customer.repository.AccountRepository;
import com.sample.banking.demo.customer.repository.AccountTransactionRepository;
import com.sample.banking.demo.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final AccountTransactionRepository accountTransactionRepository;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private AccountTransactionMapper accountTransactionMapper;

	/**
	 * 
	 * @param accountId
	 * @return
	 */
	public Optional<Account> getByAccountId(Long accountId) {
		if (accountId != null) {
			Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
			if (accountEntity.isPresent()) {
				BigDecimal accountBalance = accountTransactionRepository
						.findByAccountId(accountEntity.get().getAccountId()).stream()
						.map(tran -> tran.getTransactionAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
				Account accountDetails = accountMapper.toDto(accountEntity.get());
				accountDetails.setAccountBalance(accountBalance);
				return Optional.of(accountDetails);
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
			CustomerDetails customerDetails = CustomerDetails.builder().accountDetails(new ArrayList<>()).build();
			if (!CollectionUtils.isEmpty(accountEntities)) {
				accountEntities.stream().forEach(accountEntity -> {
					AccountDetails accountDetails = AccountDetails.builder().account(accountMapper.toDto(accountEntity))
							.build();
					BigDecimal accountBalance = accountTransactionRepository
							.findByAccountId(accountEntity.getAccountId()).stream()
							.map(tran -> tran.getTransactionAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
					accountDetails.getAccount().setAccountBalance(accountBalance);
					customerDetails.getAccountDetails().add(accountDetails);

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
	 * @throws BankingClientException
	 */
	public Account createAccount(Account account) throws BankingClientException {
		List<String> validationErrors = validate(account);
		if (CollectionUtils.isEmpty(validationErrors)) {
			AccountEntity accountEntity = accountMapper.toEntity(account);
			accountEntity = accountRepository.save(accountEntity);
			account.getAccountInitialTransaction().setTransactionTimestamp(LocalDateTime.now());
			account.getAccountInitialTransaction().setAccountId(accountEntity.getAccountId());
			accountTransactionRepository
					.save(accountTransactionMapper.toEntity(account.getAccountInitialTransaction()));
			Account accountResponse = accountMapper.toDto(accountEntity);
			accountResponse.setAccountBalance(account.getAccountInitialTransaction().getTransactionAmount());
			return accountResponse;
		} else {
			throw BankingClientException.builder().errorMessage(validationErrors.toString()).build();
		}

	}

	private List<String> validate(Account account) {
		List<String> errors = new ArrayList<String>();
		if (customerRepository.findById(account.getCustomerId()).isEmpty()) {
			errors.add("Incorrect Details, Account ID not found " + account.getCustomerId());
		}
		if (account.getAccountInitialTransaction() == null
				|| account.getAccountInitialTransaction().getTransactionAmount() == null) {
			errors.add("Initial Transaction Amount is not available" + account.getCustomerId());
		}
		if (account.getAccountInitialTransaction().getTransactionAmount().compareTo(BigDecimal.ZERO) != 1) {
			errors.add("Initial Transaction is more than 1 dollar"
					+ account.getAccountInitialTransaction().getTransactionAmount());
		}
		return errors;
	}

}
