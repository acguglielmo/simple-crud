package com.acguglielmo.simplecrud.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 7937960665097302606L;

    public EntityAlreadyExistsException(final String message) {

        super(message);

    }

}
