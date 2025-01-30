package com.sample.banking.demo.customer.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sample.banking.demo.customer.model.dto.AccountTransferFunds;
import com.sample.banking.demo.customer.model.entity.AccountTransferFundsEntity;

@Mapper(componentModel = "spring")
public interface AccountTransferFundsMapper {
	AccountTransferFunds toDto(AccountTransferFundsEntity accountTransferFundsEntity);

	List<AccountTransferFunds> toDto(List<AccountTransferFundsEntity> accountTransferFundsEntities);

	@Mapping(target = "fromAccountTransactionId", ignore = true)
	@Mapping(target = "toAccountTransactionId", ignore = true)
	AccountTransferFundsEntity toEntity(AccountTransferFunds accountTransferFunds);

}
