package org.example.readwritecache;

import java.util.concurrent.atomic.AtomicInteger;

public class CacheStats {

    private final AtomicInteger hits = new AtomicInteger(0);
    private final AtomicInteger misses = new AtomicInteger(0);

    public void recordHit() {
        hits.incrementAndGet();
    }

    public void recordMiss() {
        misses.incrementAndGet();
    }

    public int getHits() {
        return hits.get();
    }

    public int getMisses() {
        return misses.get();
    }

    @Override
    public String toString() {
        int total = hits.get() + misses.get();
        return String.format("hits=%d, misses=%d, total=%d", hits.get(), misses.get(), total);
    }
}
