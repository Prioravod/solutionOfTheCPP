package com.petrovpavel.datamodule.exceptions;

import java.io.IOException;

public class IncorrectFileStateException extends IOException {
    public IncorrectFileStateException() {
        super("Unable to read file!");
    }

    public IncorrectFileStateException(String message) {
        super(message);
    }
}
