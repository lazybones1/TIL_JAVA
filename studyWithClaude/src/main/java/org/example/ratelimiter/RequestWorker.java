package org.example.ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestWorker implements Runnable {

    private final int id;
    private final RateLimiter rateLimiter;
    private final int requestCount;
    private final AtomicInteger allowed;
    private final AtomicInteger rejected;

    public RequestWorker(int id, RateLimiter rateLimiter, int requestCount,
                         AtomicInteger allowed, AtomicInteger rejected) {
        this.id = id;
        this.rateLimiter = rateLimiter;
        this.requestCount = requestCount;
        this.allowed = allowed;
        this.rejected = rejected;
    }

    @Override
    public void run() {
        // TODO: requestCount 만큼 반복하며 rateLimiter.tryAcquire()를 호출하세요.
        //       허용되면 allowed 증가, "Worker [id] REQUEST [i] -> ALLOWED" 출력
        //       거절되면 rejected 증가, "Worker [id] REQUEST [i] -> REJECTED" 출력
        //       각 요청 사이에 Thread.sleep(50)을 넣으세요. (InterruptedException은 Thread.currentThread().interrupt()로 처리)
        for (int i = 1; i<= requestCount; i++){
            boolean acquireResult = rateLimiter.tryAcquire();
            if (acquireResult){
                allowed.incrementAndGet();
                System.out.println("Worker ["+id+"] REQUEST ["+i+"] -> ALLOWED");
            }else {
                rejected.incrementAndGet();
                System.out.println("Worker ["+id+"] REQUEST ["+i+"] -> REJECTED");
            }

            try {
                Thread.sleep(50);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
