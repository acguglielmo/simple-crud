package com.acguglielmo.simplecrud.exception;

public class ContractAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 7937960665097302606L;

    public ContractAlreadyExistsException() {

        super("Contract already exists!");

    }

}
