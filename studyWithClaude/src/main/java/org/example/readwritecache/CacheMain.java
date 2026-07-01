package org.example.readwritecache;

import java.util.ArrayList;
import java.util.List;

public class CacheMain {

    public static void main(String[] args) throws InterruptedException {
        Cache cache = new Cache();
        CacheStats stats = new CacheStats();

        int workerCount = 5;
        int readCount = 10;
        int writeCount = 3;

        // TODO: CacheWorker 스레드를 workerCount 만큼 생성하고 실행하세요.
        List<Thread> cacheWorkers = new ArrayList<>();
        for (int i = 1; i<=workerCount; i++){
            Thread t = new Thread(new CacheWorker(i, cache, stats, readCount, writeCount));
            t.start();
            cacheWorkers.add(t);
        }
        // TODO: 모든 스레드가 종료될 때까지 기다리세요. (join)
        for (Thread t : cacheWorkers){
            t.join();
        }

        System.out.println("=== 결과 ===");
        System.out.println("캐시 크기: " + cache.size());
        System.out.println("통계: " + stats);
    }
}
