package org.example.circuitbreaker;

public class CircuitBreakerWorker implements Runnable {

    private final int id;
    private final CircuitBreaker circuitBreaker;
    private final FlakyService service;
    private final int callCount;

    public CircuitBreakerWorker(int id, CircuitBreaker circuitBreaker, FlakyService service, int callCount) {
        this.id = id;
        this.circuitBreaker = circuitBreaker;
        this.service = service;
        this.callCount = callCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < callCount; i++) {
            try {
                String result = circuitBreaker.execute(service::call);
                System.out.printf("[worker-%d] 성공: %s (상태: %s)%n", id, result, circuitBreaker.getState());
            } catch (CircuitOpenException e) {
                System.out.printf("[worker-%d] fail-fast (상태: %s)%n", id, circuitBreaker.getState());
            } catch (Exception e) {
                System.out.printf("[worker-%d] 실패: %s (상태: %s)%n", id, e.getMessage(), circuitBreaker.getState());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
