package org.example.distributedlock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public class DistributedLockMain {

    private static final String COUNTER_KEY = "counter";

    public static void main(String[] args) throws InterruptedException {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        DistributedLock lock = new RedisDistributedLock(jedisPool);

        int workerCount = 5;
        int incrementPerWorker = 100;
        int expected = workerCount * incrementPerWorker;

        System.out.println("=== 락 없이 실행 (race condition 재현) ===");
        runTest(jedisPool, lock, workerCount, incrementPerWorker, false);
        System.out.println("기대값: " + expected + " / 실제값: " + getCounter(jedisPool));

        System.out.println();

        System.out.println("=== 분산 락 적용 ===");
        runTest(jedisPool, lock, workerCount, incrementPerWorker, true);
        System.out.println("기대값: " + expected + " / 실제값: " + getCounter(jedisPool));

        jedisPool.close();
    }

    private static void runTest(JedisPool jedisPool, DistributedLock lock,
                                 int workerCount, int incrementPerWorker, boolean useLock)
            throws InterruptedException {
        resetCounter(jedisPool);

        List<Thread> workers = new ArrayList<>();
        for (int i = 1; i <= workerCount; i++) {
            Thread t = new Thread(new RedisCounterWorker(i, jedisPool, lock, incrementPerWorker, useLock));
            t.start();
            workers.add(t);
        }
        for (Thread t : workers) {
            t.join();
        }
    }

    private static void resetCounter(JedisPool jedisPool) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(COUNTER_KEY, "0");
        }
    }

    private static int getCounter(JedisPool jedisPool) {
        try (Jedis jedis = jedisPool.getResource()) {
            return Integer.parseInt(jedis.get(COUNTER_KEY));
        }
    }
}
