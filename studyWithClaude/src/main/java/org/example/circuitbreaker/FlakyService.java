package org.example.circuitbreaker;

import java.util.concurrent.ThreadLocalRandom;

public class FlakyService {

    private final double failureRate;

    public FlakyService(double failureRate) {
        this.failureRate = failureRate;
    }

    public String call() {
        if (ThreadLocalRandom.current().nextDouble() < failureRate) {
            throw new RuntimeException("downstream service failure");
        }
        return "OK";
    }
}
