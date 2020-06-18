package com.acguglielmo.simplecrud.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3824633443116528534L;

	public NotFoundException(final String message) {

		super(message);

	}

}
