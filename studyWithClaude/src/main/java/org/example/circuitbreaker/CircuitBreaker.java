package org.example.circuitbreaker;

import java.util.concurrent.Callable;

public interface CircuitBreaker {
    <T> T execute(Callable<T> action) throws Exception;
    CircuitState getState();
}
