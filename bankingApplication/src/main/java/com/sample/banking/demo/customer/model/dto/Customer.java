package com.sample.banking.demo.customer.model.dto;

import java.time.LocalDate;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
	private Long customerId;
	@NonNull
	private String userName;
	@NonNull
	@Email
	private String email;
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	@NonNull
	@JsonFormat(pattern = "MM/dd/yyyy")
	private LocalDate dob;
	@NonNull
	private String ssn;
}
