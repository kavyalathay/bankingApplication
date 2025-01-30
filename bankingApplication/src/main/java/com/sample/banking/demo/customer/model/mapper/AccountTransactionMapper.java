package com.sample.banking.demo.customer.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sample.banking.demo.customer.model.dto.AccountTransaction;
import com.sample.banking.demo.customer.model.entity.AccountTransactionEntity;

@Mapper(componentModel = "spring")
public interface AccountTransactionMapper {
	AccountTransaction toDto(AccountTransactionEntity accountTransactionEntity);

	List<AccountTransaction> toDto(List<AccountTransactionEntity> accountTransactionEntity);

	AccountTransactionEntity toEntity(AccountTransaction accountTransaction);

}
