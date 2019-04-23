package com.petrovpavel.datamodule.exceptions;

public class TaskBuilderException extends RuntimeException {
    public TaskBuilderException() {
        super("The object is not ready!");
    }

    public TaskBuilderException(String message) {
        super(message);
    }
}
