package org.example.circuitbreaker;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

class SimpleCircuitBreakerTest {

    // 연속 실패가 failureThreshold에 도달하면 CLOSED -> OPEN으로 전환되는지 검증하세요.
    @Test
    void test01(){
        FakeCurrentTime fakeCurrentTime = new FakeCurrentTime(0);
        CircuitBreaker circuitBreaker = new SimpleCircuitBreaker(3, 2000, 2, fakeCurrentTime);

        for (int i = 0; i < 2; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("fail");
            }));
        }
        assertEquals(CircuitState.CLOSED, circuitBreaker.getState());

        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
            throw new RuntimeException("fail");
        }));
        assertEquals(CircuitState.OPEN, circuitBreaker.getState());
    }
    // TODO: OPEN 상태에서 openTimeoutMillis가 지나기 전에 호출하면 CircuitOpenException이 발생하는지 검증하세요.
    //       (FakeCurrentTime을 사용해서 시간 흐름을 직접 통제하세요.)
    @Test
    void test02(){
        FakeCurrentTime fakeCurrentTime = new FakeCurrentTime(0);
        CircuitBreaker circuitBreaker = new SimpleCircuitBreaker(3, 2000, 2, fakeCurrentTime);

        for (int i = 0; i < 3; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("fail");
            }));
        }
        assertEquals(CircuitState.OPEN, circuitBreaker.getState());

        AtomicBoolean actionCalled = new AtomicBoolean(false);
        assertThrows(CircuitOpenException.class, () -> circuitBreaker.execute(() -> {
            actionCalled.set(true);
            return "OK";
        }));
        assertFalse(actionCalled.get());
        assertEquals(CircuitState.OPEN, circuitBreaker.getState());
    }
    // OPEN 상태에서 openTimeoutMillis가 지난 뒤 호출하면 HALF_OPEN으로 전환되고
    // 실제 action이 실행되는지 검증하세요.
    @Test
    void test03(){
        FakeCurrentTime fakeCurrentTime = new FakeCurrentTime(0);
        CircuitBreaker circuitBreaker = new SimpleCircuitBreaker(3, 2000, 2, fakeCurrentTime);

        for (int i = 0; i < 3; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("fail");
            }));
        }
        assertEquals(CircuitState.OPEN, circuitBreaker.getState());

        fakeCurrentTime.advance(2000);

        AtomicBoolean actionCalled = new AtomicBoolean(false);
        assertDoesNotThrow(() -> circuitBreaker.execute(() -> {
            actionCalled.set(true);
            // action이 실행되는 시점엔 이미 HALF_OPEN으로 전환되어 있어야 한다.
            assertEquals(CircuitState.HALF_OPEN, circuitBreaker.getState());
            return "OK";
        }));
        assertTrue(actionCalled.get());
    }

    // HALF_OPEN 상태에서 action이 성공하면 CLOSED로 전환되는지 검증하세요.
    @Test
    void test04() throws Exception {
        FakeCurrentTime fakeCurrentTime = new FakeCurrentTime(0);
        CircuitBreaker circuitBreaker = new SimpleCircuitBreaker(3, 2000, 2, fakeCurrentTime);

        for (int i = 0; i < 3; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("fail");
            }));
        }
        fakeCurrentTime.advance(2000);

        circuitBreaker.execute(() -> "OK");
        assertEquals(CircuitState.CLOSED, circuitBreaker.getState());

        // failureCount가 완전히 초기화됐다면, 다시 threshold-1번 실패로는 OPEN이 되지 않아야 한다.
        for (int i = 0; i < 2; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("fail");
            }));
        }
        assertEquals(CircuitState.CLOSED, circuitBreaker.getState());
    }

    // HALF_OPEN 상태에서 action이 실패하면 다시 OPEN으로 전환되는지 검증하세요.
    @Test
    void test05(){
        FakeCurrentTime fakeCurrentTime = new FakeCurrentTime(0);
        CircuitBreaker circuitBreaker = new SimpleCircuitBreaker(3, 2000, 2, fakeCurrentTime);

        for (int i = 0; i < 3; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("fail");
            }));
        }
        fakeCurrentTime.advance(2000);

        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
            throw new RuntimeException("fail again");
        }));
        assertEquals(CircuitState.OPEN, circuitBreaker.getState());
    }
}
