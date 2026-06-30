package org.example.connectionpool;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPoolMain {

    public static void main(String[] args) throws InterruptedException {
        int poolSize = 3;
        int workerCount = 8;

        ConnectionPool pool = new ConnectionPool(poolSize);

        // TODO: Worker 스레드를 workerCount 만큼 생성하고 실행하세요.
        List<Thread> workers = new ArrayList<>();
        for (int i = 1; i<= workerCount; i++){
            Thread t = new Thread(new Worker(i, pool));
            t.start();
            workers.add(t);
        }
        // TODO: 모든 Worker 스레드가 종료될 때까지 기다리세요. (join)
        for (Thread t : workers){
            t.join();
        }

        System.out.println("모든 작업 완료");
    }
}
