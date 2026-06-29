package org.example.producerconsumer;

import java.util.concurrent.atomic.AtomicInteger;

public class ItemIdCounter {
    private static final AtomicInteger count = new AtomicInteger(0);

    public static int next() {
        return count.incrementAndGet(); // 증가와 읽기를 원자적으로
    }
}
