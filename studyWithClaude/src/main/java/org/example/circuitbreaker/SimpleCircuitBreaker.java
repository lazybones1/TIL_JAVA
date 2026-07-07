package org.example.circuitbreaker;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleCircuitBreaker implements CircuitBreaker {

    private final int failureThreshold;
    private final long openTimeoutMillis;
    private final int halfOpenMaxAttempts;
    private final CurrentTime currentTime;
    private final ReentrantLock lock = new ReentrantLock();

    private CircuitState state = CircuitState.CLOSED;
    private int failureCount = 0;
    private long openedAt = 0;
    private int halfOpenAttempts = 0;

    public SimpleCircuitBreaker(int failureThreshold, long openTimeoutMillis, int halfOpenMaxAttempts,
                                 CurrentTime currentTime) {
        this.failureThreshold = failureThreshold;
        this.openTimeoutMillis = openTimeoutMillis;
        this.halfOpenMaxAttempts = halfOpenMaxAttempts;
        this.currentTime = currentTime;
    }

    public SimpleCircuitBreaker(int failureThreshold, long openTimeoutMillis, int halfOpenMaxAttempts) {
        this(failureThreshold, openTimeoutMillis, halfOpenMaxAttempts, System::currentTimeMillis);
    }

    @Override
    public <T> T execute(Callable<T> action) throws Exception {
        // TODO: 실행 전, 현재 상태에 따라 통과시킬지 fail-fast 시킬지 결정하세요.
        //
        //   state == OPEN 인 경우:
        //     - openedAt 으로부터 openTimeoutMillis 가 지났는지 currentTime.getCurrentTime()으로 확인하세요.
        //     - 아직 안 지났으면 CircuitOpenException을 던져서 즉시 실패 처리(fail-fast) 하세요.
        //     - 지났으면 state를 HALF_OPEN으로 바꾸고 halfOpenAttempts를 0으로 초기화한 뒤 통과시키세요.
        //
        //   state == HALF_OPEN 인 경우:
        //     - halfOpenAttempts가 이미 halfOpenMaxAttempts에 도달했으면 CircuitOpenException을 던지세요.
        //       (탐색용으로 허용한 시도 수를 넘겼으니 더는 통과시키지 않음)
        //     - 아니면 halfOpenAttempts를 늘리고 통과시키세요.
        //
        //   state == CLOSED 인 경우: 그냥 통과.
        //
        //   이 블록 전체는 여러 스레드가 동시에 호출할 수 있으니 lock으로 감싸세요.
        try{
            lock.lock();
            if (state.equals(CircuitState.OPEN)) {
                if (openedAt + openTimeoutMillis > currentTime.getCurrentTime()){
                    throw new CircuitOpenException("fail");
                }else {
                    state = CircuitState.HALF_OPEN;
                    halfOpenAttempts = 1;
                }
            }else if(state.equals(CircuitState.HALF_OPEN)){
                if (halfOpenAttempts >= halfOpenMaxAttempts){
                    throw new CircuitOpenException("fail");
                }else {
                    halfOpenAttempts++;
                }
            } else if (state.equals(CircuitState.CLOSED)) {

            }
        }finally {
            lock.unlock();
        }

        try {
            T result = action.call();
            recordSuccess();
            return result;
        } catch (Exception e) {
            recordFailure();
            throw e;
        }
    }

    private void recordSuccess() {
        lock.lock();
        try {
            // TODO: state == HALF_OPEN 이면 다운스트림이 회복된 것으로 보고 CLOSED로 전환하고,
            //       failureCount와 halfOpenAttempts를 초기화하세요.
            //       state == CLOSED 이면 failureCount를 0으로 리셋하세요. (연속 실패 기준이므로)
            if (state.equals(CircuitState.HALF_OPEN)){
                state = CircuitState.CLOSED;
                failureCount = 0;
                halfOpenAttempts = 0;
            } else if (state.equals(CircuitState.CLOSED)) {
                failureCount = 0;
            }
        } finally {
            lock.unlock();
        }
    }

    private void recordFailure() {
        lock.lock();
        try {
            // TODO: state == HALF_OPEN 이면 탐색 요청이 실패한 것이므로 즉시 다시 OPEN으로 전환하고
            //       openedAt을 현재 시각으로 갱신하세요.
            //       state == CLOSED 이면 failureCount를 늘리고, failureThreshold에 도달하면
            //       OPEN으로 전환 + openedAt을 현재 시각으로 갱신하세요.
            if (state.equals(CircuitState.HALF_OPEN)){
                state = CircuitState.OPEN;
                openedAt = currentTime.getCurrentTime();
            } else if (state.equals(CircuitState.CLOSED)) {
                failureCount++;
                if (failureThreshold == failureCount){
                    state = CircuitState.OPEN;
                    openedAt = currentTime.getCurrentTime();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public CircuitState getState() {
        lock.lock();
        try {
            return state;
        } finally {
            lock.unlock();
        }
    }
}
