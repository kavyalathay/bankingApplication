package com.sample.banking.demo.customer.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BankingClientException.class)
	private ResponseEntity<String> handleBankingClientException(BankingClientException exception) {
		return ResponseEntity.badRequest().body(exception.getErrorMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	private ResponseEntity<String> handleUnknownException(RuntimeException exception) {
		log.error("Error: {}", exception.getLocalizedMessage(), exception);
		return ResponseEntity.internalServerError().body(exception.getLocalizedMessage());
	}

}
