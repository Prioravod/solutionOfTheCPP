package com.petrovpavel.datamodule.exceptions;

public class AdjacencyMatrixSizeException extends RuntimeException {
    public AdjacencyMatrixSizeException() {
        super("The size of the adjacency matrix can not be less than 2!");
    }

    public AdjacencyMatrixSizeException(String message) {
        super(message);
    }
}
