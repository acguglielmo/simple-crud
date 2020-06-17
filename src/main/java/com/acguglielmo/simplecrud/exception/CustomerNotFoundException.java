package com.acguglielmo.simplecrud.exception;

public class CustomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3824633443116528534L;

	public CustomerNotFoundException() {

		super("Customer not found!");

	}

}
