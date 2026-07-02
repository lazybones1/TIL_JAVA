package org.example.ratelimiter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final long windowMillis;
    private final Deque<Long> requestTimestamps = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final CurrentTime currentTime;

    public SlidingWindowRateLimiter(int maxRequests, long windowMillis, CurrentTime currentTime) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;

        this.currentTime = currentTime;
    }
    public SlidingWindowRateLimiter(int maxRequests, long windowMillis) {
        this(maxRequests, windowMillis, System::currentTimeMillis);
    }


    @Override
    public boolean tryAcquire() {
        lock.lock();
        try {
            long now = currentTime.getCurrentTime();
            while (!requestTimestamps.isEmpty()){
                // TODO: windowMillis 이전보다 오래된 타임스탬프를 requestTimestamps 앞에서 제거하세요.
                if (requestTimestamps.peek() < now - windowMillis){
                    requestTimestamps.poll();
                }else {
                    break;
                }
            }
            // TODO: requestTimestamps 크기가 maxRequests 미만이면 now를 추가하고 true를 반환하세요.
            if (requestTimestamps.size() < maxRequests){
                requestTimestamps.add(now);
                return true;
            }
            // TODO: 초과된 경우 false를 반환하세요.
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String getName() {
        return "SlidingWindow";
    }

}
