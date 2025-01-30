package com.sample.banking.demo;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.sample.banking.demo.customer.model.entity.AccountEntity;
import com.sample.banking.demo.customer.model.entity.AccountTransactionEntity;
import com.sample.banking.demo.customer.model.entity.CustomerEntity;
import com.sample.banking.demo.customer.repository.AccountRepository;
import com.sample.banking.demo.customer.repository.AccountTransactionRepository;
import com.sample.banking.demo.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class DataLoader {
	private final CustomerRepository customerRepository;
	private final AccountRepository accountRepository;
	private final AccountTransactionRepository accountTransactionRepository;

//	@Bean
//	public ObjectMapper createObjectMapper(ObjectMapper mapper) {
//		JavaTimeModule javaTimeModule = new JavaTimeModule();
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE));
//		mapper.registerModule(javaTimeModule);
//		return mapper;
//	}

	@Bean
	CommandLineRunner loadCustomerData(ObjectMapper mapper) {
//		JavaTimeModule javaTimeModule = new JavaTimeModule();
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
//		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE));
//		mapper.registerModule(javaTimeModule);

		return args -> {
			Resource resource = new ClassPathResource("/data/customers.json");
			try (InputStream is = resource.getInputStream()) {
				List<CustomerEntity> customers = mapper.readValue(is, new TypeReference<List<CustomerEntity>>() {
				});

				customerRepository.saveAll(customers);
			}
		};
	}

	@Bean
	CommandLineRunner loadAccountsData(ObjectMapper objectMapper) {
		return args -> {
			Resource resource = new ClassPathResource("/data/accounts.json");
			try (InputStream is = resource.getInputStream()) {
				List<AccountEntity> customers = objectMapper.readValue(is, new TypeReference<List<AccountEntity>>() {
				});
				accountRepository.saveAll(customers);
			}
		};
	}

	@Bean
	CommandLineRunner loadAccountTransactionsData(ObjectMapper objectMapper) {
		return args -> {
			Resource resource = new ClassPathResource("/data/accountTransactions.json");
			try (InputStream is = resource.getInputStream()) {
				List<AccountTransactionEntity> accountTransactions = objectMapper.readValue(is,
						new TypeReference<List<AccountTransactionEntity>>() {
						});
				accountTransactionRepository.saveAll(accountTransactions);
			}
		};
	}

}
