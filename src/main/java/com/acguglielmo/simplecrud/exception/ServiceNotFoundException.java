package com.acguglielmo.simplecrud.exception;

public class ServiceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3458575854227432895L;

	public ServiceNotFoundException() {

		super("Service not found!");

	}

}
