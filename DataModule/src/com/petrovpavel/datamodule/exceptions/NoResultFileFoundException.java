package com.petrovpavel.datamodule.exceptions;

public class NoResultFileFoundException extends RuntimeException {
    public NoResultFileFoundException() {
        super();
    }

    public NoResultFileFoundException(String message) {
        super(message);
    }
}
