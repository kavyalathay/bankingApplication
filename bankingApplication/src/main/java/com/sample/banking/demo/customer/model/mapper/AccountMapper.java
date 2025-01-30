package com.sample.banking.demo.customer.model.mapper;

import org.mapstruct.Mapper;

import com.sample.banking.demo.customer.model.dto.Account;
import com.sample.banking.demo.customer.model.entity.AccountEntity;

@Mapper(componentModel = "spring")
public interface AccountMapper {
	Account toDto(AccountEntity accountEntity);
	AccountEntity toEntity(Account account);
}
