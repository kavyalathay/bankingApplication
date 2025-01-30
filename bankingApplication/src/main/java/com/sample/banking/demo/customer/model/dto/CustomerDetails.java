package com.sample.banking.demo.customer.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetails {
	private Customer customer;
	private List<AccountDetails> accountDetails;
}
