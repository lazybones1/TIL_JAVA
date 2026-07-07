package org.example.circuitbreaker;

public class CircuitOpenException extends RuntimeException {
    public CircuitOpenException(String message) {
        super(message);
    }
}
