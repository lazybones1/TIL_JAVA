package org.example.distributedlock;

public interface DistributedLock {
    boolean tryLock(String key, String requestId, long ttlMillis);
    boolean unlock(String key, String requestId);
}
