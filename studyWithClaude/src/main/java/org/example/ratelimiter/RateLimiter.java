package org.example.ratelimiter;

public interface RateLimiter {
    boolean tryAcquire();
    String getName();
}
