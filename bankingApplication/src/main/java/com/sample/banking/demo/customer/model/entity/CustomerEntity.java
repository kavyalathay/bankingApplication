package com.sample.banking.demo.customer.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;
	@Column(unique = true)
	private String userName;
	@Column(unique = true)
	private String email;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String ssn;
//	@OneToMany
//	@JoinColumn(name = "customer_id")
//	private List<AccountEntity> accounts;
}
