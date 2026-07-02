package org.example.ratelimiter;

import java.util.concurrent.locks.ReentrantLock;

public class TokenBucketRateLimiter implements RateLimiter {

    private final int capacity;
    private final double refillRatePerSecond;
    private double currentTokens;
    private long lastRefillTimestamp;
    private final ReentrantLock lock = new ReentrantLock();
    private final CurrentTime currentTime;

    public TokenBucketRateLimiter(int capacity, double refillRatePerSecond, CurrentTime currentTime) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.currentTokens = capacity;
        this.currentTime = currentTime;
        this.lastRefillTimestamp = currentTime.getCurrentTime();
    }

    public TokenBucketRateLimiter(int capacity, double refillRatePerSecond) {
        this(capacity, refillRatePerSecond, System::currentTimeMillis);
    }


    @Override
    public boolean tryAcquire() {
        lock.lock();
        try {
            long now = currentTime.getCurrentTime();
            // TODO: 현재 시간과 lastRefillTimestamp의 차이(초)를 계산하세요.
            double timeDiff = (now - lastRefillTimestamp) / 1000.0;
            // TODO: 경과 시간 * refillRatePerSecond 만큼 토큰을 보충하세요. (capacity 초과 불가)
            double calulateCurrentToken = currentTokens + (timeDiff * refillRatePerSecond) ;
            currentTokens = calulateCurrentToken >= capacity ? capacity : calulateCurrentToken;
            // TODO: lastRefillTimestamp를 현재 시간으로 갱신하세요.
            // lastRefillTimestamp = System.currentTimeMillis();
            lastRefillTimestamp = now;
            // TODO: currentTokens >= 1 이면 토큰 1개를 소비하고 true를 반환하세요.
            if (currentTokens >= 1){
                currentTokens--;
                return true;
            }
            // TODO: 토큰이 없으면 false를 반환하세요.
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String getName() {
        return "TokenBucket";
    }
}
