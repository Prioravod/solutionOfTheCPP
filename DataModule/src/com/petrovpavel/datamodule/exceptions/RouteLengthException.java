package com.petrovpavel.datamodule.exceptions;

public class RouteLengthException extends RuntimeException {
    public RouteLengthException() {
        super("The length of the route cannot be zero, negative or " +
                "greater than the dimension of the adjacency matrix!");
    }

    public RouteLengthException(String message) {
        super(message);
    }
}
