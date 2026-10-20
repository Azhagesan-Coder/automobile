package com.Customer.CustomerManagement.Exception;

@org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.CONFLICT)
public class DuplicateEntryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicateEntryException(String message) {
		super(message);
	}

	
}
