package com.sample.banking.demo.customer.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.sample.banking.demo.customer.exception.BankingClientException;
import com.sample.banking.demo.customer.model.dto.AccountDetails;
import com.sample.banking.demo.customer.model.dto.AccountTransaction;
import com.sample.banking.demo.customer.model.dto.AccountTransferFunds;
import com.sample.banking.demo.customer.model.dto.CustomerDetails;
import com.sample.banking.demo.customer.model.entity.AccountEntity;
import com.sample.banking.demo.customer.model.entity.AccountTransactionEntity;
import com.sample.banking.demo.customer.model.entity.AccountTransferFundsEntity;
import com.sample.banking.demo.customer.model.entity.CustomerEntity;
import com.sample.banking.demo.customer.model.mapper.AccountMapper;
import com.sample.banking.demo.customer.model.mapper.AccountTransactionMapper;
import com.sample.banking.demo.customer.model.mapper.AccountTransferFundsMapper;
import com.sample.banking.demo.customer.model.mapper.CustomerMapper;
import com.sample.banking.demo.customer.repository.AccountRepository;
import com.sample.banking.demo.customer.repository.AccountTransactionRepository;
import com.sample.banking.demo.customer.repository.AccountTransferFundsRepository;
import com.sample.banking.demo.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final AccountTransactionRepository accountTransactionRepository;
	private final AccountTransferFundsRepository accountTransferFundsRepository;

	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private AccountTransactionMapper accountTransactionMapper;
	@Autowired
	private AccountTransferFundsMapper accountTransferFundsMapper;

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
		CustomerDetails cstomerDetails = CustomerDetails.builder().accountDetails(new ArrayList<>()).build();
		if (customerEntity.isPresent()) {
			cstomerDetails.setCustomer(customerMapper.toDto(customerEntity.get()));
			// all accounts
			List<AccountEntity> accountEntities = accountRepository.findByCustomerId(customerId);
			accountEntities.stream().forEach(accountEntity -> {
				// add account details
				AccountDetails accountDetails = AccountDetails.builder().account(accountMapper.toDto(accountEntity))
						.transactions(new ArrayList<>()).build();
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

	public Optional<AccountTransaction> createTransaction(AccountTransaction accountTransaction)
			throws BankingClientException {
		List<String> validationErrors = validate(accountTransaction);
		if (CollectionUtils.isEmpty(validationErrors)) {
			accountTransaction.setTransactionTimestamp(LocalDateTime.now());
			AccountTransactionEntity entity = accountTransactionRepository
					.save(accountTransactionMapper.toEntity(accountTransaction));
			return Optional.of(accountTransactionMapper.toDto(entity));
		}
		throw BankingClientException.builder().errorMessage(validationErrors.toString()).build();
	}

	private List<String> validate(AccountTransaction accountTransaction) {
		List<String> errorMessages = new ArrayList<String>();
		if (accountRepository.findById(accountTransaction.getAccountId()).isEmpty()) {
			errorMessages.add("Account ID is not valid");
		}
		if (accountTransaction.getTransactionAmount() == null
				|| accountTransaction.getTransactionAmount().compareTo(BigDecimal.ZERO) < 1) {
			errorMessages.add("TransactionAmount is not valid");
		}
		if (accountTransaction.getTransactionName() == null) {
			errorMessages.add("Transaction Name is not valid");
		}
		return errorMessages;
	}

	@Transactional
	public AccountTransferFunds createFundsTransfer(AccountTransferFunds accountTransferFunds)
			throws BankingClientException {
		List<String> validationErrors = validate(accountTransferFunds);
		if (CollectionUtils.isEmpty(validationErrors)) {
			AccountTransactionEntity fromAccountTransaction = AccountTransactionEntity.builder()
					.accountId(accountTransferFunds.getFromAccountId())
					.transactionAmount(accountTransferFunds.getTransactionAmount().negate())
					.transactionName(accountTransferFunds.getTransactionName())
					.transactionTimestamp(LocalDateTime.now()).build();
			AccountTransactionEntity response1 = accountTransactionRepository.save(fromAccountTransaction);

			AccountTransactionEntity toAccountTransactionEntity = AccountTransactionEntity.builder()
					.accountId(accountTransferFunds.getToAccountId())
					.transactionAmount(accountTransferFunds.getTransactionAmount())
					.transactionName(accountTransferFunds.getTransactionName())
					.transactionTimestamp(LocalDateTime.now()).build();
			AccountTransactionEntity response2 = accountTransactionRepository.save(toAccountTransactionEntity);

			accountTransferFunds.setTransactionTimestamp(LocalDateTime.now());

			AccountTransferFundsEntity entity = accountTransferFundsMapper.toEntity(accountTransferFunds);
			entity.setFromAccountTransactionId(response1.getTransactionId());
			entity.setToAccountTransactionId(response2.getTransactionId());

			entity = accountTransferFundsRepository.save(accountTransferFundsMapper.toEntity(accountTransferFunds));
			return accountTransferFundsMapper.toDto(entity);
		} else {
			throw BankingClientException.builder().errorMessage(validationErrors.toString()).build();
		}
	}

	private List<String> validate(AccountTransferFunds accountTransferFunds) {
		List<String> errorMessages = new ArrayList<String>();
		Optional<AccountEntity> fromAccount = accountRepository.findById(accountTransferFunds.getFromAccountId());
		if (fromAccount.isEmpty()) {
			errorMessages.add("From Account Id is not valid" + accountTransferFunds.getFromAccountId());
		}
		BigDecimal accountBalance = accountTransactionRepository.findById(fromAccount.get().getAccountId()).stream()
				.map(tran -> tran.getTransactionAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (accountBalance.compareTo(accountTransferFunds.getTransactionAmount()) < 1) {
			errorMessages.add("Not sufficient balance to Transger" + accountTransferFunds.getToAccountId());
		}
		if (accountRepository.findById(accountTransferFunds.getToAccountId()).isEmpty()) {
			errorMessages.add("To Account Id is not valid" + accountTransferFunds.getToAccountId());
		}
		return errorMessages;
	}

}
