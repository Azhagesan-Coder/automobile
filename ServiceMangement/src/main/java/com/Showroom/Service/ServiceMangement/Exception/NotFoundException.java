package com.Showroom.Service.ServiceMangement.Exception;

@org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundException(String message) {
		super(message);
	}

}
