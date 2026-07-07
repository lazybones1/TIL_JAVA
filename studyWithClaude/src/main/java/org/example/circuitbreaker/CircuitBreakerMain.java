package org.example.circuitbreaker;

import java.util.ArrayList;
import java.util.List;

public class CircuitBreakerMain {

    public static void main(String[] args) throws InterruptedException {
        // 80% 확률로 실패하는 다운스트림 서비스를 흉내냄
        FlakyService service = new FlakyService(0.8);

        // 연속 3회 실패 시 OPEN, 2초 후 HALF_OPEN, HALF_OPEN에서 동시 2건까지만 시도
        CircuitBreaker circuitBreaker = new SimpleCircuitBreaker(3, 2000, 2);

        List<Thread> workers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Thread t = new Thread(new CircuitBreakerWorker(i, circuitBreaker, service, 20));
            t.start();
            workers.add(t);
        }
        for (Thread t : workers) {
            t.join();
        }
    }
}
