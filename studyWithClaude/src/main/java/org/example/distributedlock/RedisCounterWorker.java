package org.example.distributedlock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.UUID;

public class RedisCounterWorker implements Runnable {

    private static final String COUNTER_KEY = "counter";
    private static final String LOCK_KEY = "counter:lock";
    private static final long LOCK_TTL_MILLIS = 3000;
    private static final long RETRY_INTERVAL_MILLIS = 50;

    private final int id;
    private final JedisPool jedisPool;
    private final DistributedLock lock;
    private final int incrementCount;
    private final boolean useLock;

    public RedisCounterWorker(int id, JedisPool jedisPool, DistributedLock lock,
                               int incrementCount, boolean useLock) {
        this.id = id;
        this.jedisPool = jedisPool;
        this.lock = lock;
        this.incrementCount = incrementCount;
        this.useLock = useLock;
    }

    @Override
    public void run() {
        for (int i = 0; i < incrementCount; i++) {
            if (useLock) {
                // TODO: UUID.randomUUID().toString()로 고유 requestId를 만드세요.
                //       lock.tryLock(LOCK_KEY, requestId, LOCK_TTL_MILLIS)가 false면
                //       짧게 Thread.sleep() 후 재시도하세요 (busy-wait retry).
                //       성공하면 increment()를 실행하고, 반드시 try-finally로 lock.unlock()을 호출하세요.
                String requestId = UUID.randomUUID().toString();
                while (!lock.tryLock(LOCK_KEY, requestId, LOCK_TTL_MILLIS)){
                    try {
                        Thread.sleep(RETRY_INTERVAL_MILLIS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                try {
                    increment();
                }finally {
                    lock.unlock(LOCK_KEY, requestId);
                }
            } else {
                // 락 없이 그냥 증가시킵니다. (race condition 재현용 — 건드리지 마세요)
                increment();
            }
        }
    }

    private void increment() {
        try (Jedis jedis = jedisPool.getResource()) {
            // TODO: GET counter로 현재 값을 읽고, 정수로 변환 후 +1 해서 SET counter로 다시 저장하세요.
            //       (이 "읽고 -> 계산하고 -> 쓰는" 구간 사이에 다른 스레드가 끼어드는 게
            //        바로 race condition입니다. 락 없이 실행하면 최종 값이 기대값보다 작게 나옵니다.)
            jedis.set(COUNTER_KEY, String.valueOf(Long.parseLong(jedis.get(COUNTER_KEY)) + 1));
        }
    }
}
