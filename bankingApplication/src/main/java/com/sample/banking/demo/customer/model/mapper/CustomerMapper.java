package com.sample.banking.demo.customer.model.mapper;

import org.mapstruct.Mapper;

import com.sample.banking.demo.customer.model.dto.Customer;
import com.sample.banking.demo.customer.model.entity.CustomerEntity;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
	Customer toDto(CustomerEntity customerEntity);
	CustomerEntity toEntity(Customer customer);
}
