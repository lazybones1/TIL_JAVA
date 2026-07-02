package org.example.ratelimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiterMain {

    public static void main(String[] args) throws InterruptedException {
        // 테스트 1: TokenBucket - 용량 5, 초당 2개 보충
        System.out.println("=== TokenBucket Rate Limiter ===");
        runTest(new TokenBucketRateLimiter(5, 2.0), 3, 5);

        System.out.println();

        // 테스트 2: SlidingWindow - 1초 윈도우에 최대 5개
        System.out.println("=== SlidingWindow Rate Limiter ===");
        runTest(new SlidingWindowRateLimiter(5, 1000), 3, 5);
    }

    private static void runTest(RateLimiter rateLimiter, int workerCount, int requestCount)
            throws InterruptedException {
        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger rejected = new AtomicInteger(0);

        // TODO: RequestWorker 스레드를 workerCount 만큼 생성하고 실행하세요.
        List<Thread> requestWorkers = new ArrayList<>();
        for (int i = 1; i<=workerCount; i++){
            Thread t = new Thread(new RequestWorker(i, rateLimiter, requestCount, allowed, rejected));
            t.start();
            requestWorkers.add(t);
        }
        // TODO: 모든 스레드가 종료될 때까지 기다리세요. (join)
        for (Thread t : requestWorkers){
            t.join();
        }

        System.out.println("=== 결과 ===");
        System.out.println("허용: " + allowed.get());
        System.out.println("거절: " + rejected.get());
        System.out.printf("허용률: %.1f%%%n", (double) allowed.get() / (allowed.get() + rejected.get()) * 100);
    }
}
