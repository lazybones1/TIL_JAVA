package org.example.readwritecache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache {

    private final Map<String, String> store = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void put(String key, String value) {
        // TODO: 쓰기 락을 획득하세요.
        // TODO: store에 key-value를 저장하세요.
        // TODO: 쓰기 락을 해제하세요. (finally 블록에서)
        lock.writeLock().lock();
        try {
            store.put(key, value);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public String get(String key) {
        // TODO: 읽기 락을 획득하세요.
        // TODO: store에서 key에 해당하는 값을 반환하세요. (없으면 null)
        // TODO: 읽기 락을 해제하세요. (finally 블록에서)
        String res = null;
        lock.readLock().lock();
        try {
            res = store.get(key);
        }finally {
            lock.readLock().unlock();
        }
        return res;
    }

    public int size() {
        // TODO: 읽기 락을 획득하세요.
        // TODO: store의 크기를 반환하세요.
        // TODO: 읽기 락을 해제하세요. (finally 블록에서)
        int res = 0;
        lock.readLock().lock();
        try {
            res = store.size();
        }finally {
            lock.readLock().unlock();
        }
        return res;
    }
}
