package com.petrovpavel.datamodule.exceptions;

public class CommandExecutionException extends RuntimeException {
    public CommandExecutionException() {
        super();
    }

    public CommandExecutionException(String message) {
        super(message);
    }
}
