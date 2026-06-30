package org.example.connectionpool;

import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.LinkedList;

public class ConnectionPool {

    private final Semaphore semaphore;
    private final Queue<Connection> connections;

    public ConnectionPool(int poolSize) {
        // TODO: Semaphore를 poolSize로 초기화하세요. (공정성 보장: fair=true)
        semaphore = new Semaphore(poolSize, true);
        // TODO: connections 큐에 Connection 객체를 poolSize개 생성해서 넣으세요.
        //       Connection id는 1부터 시작하세요.
        this.connections = new LinkedList<>();
        for (int i = 1; i<=poolSize; i++){
            connections.add(new Connection(i));
        }
    }

    public Connection acquire() throws InterruptedException {
        // TODO: Semaphore를 획득하세요. (퍼밋이 없으면 대기)
        semaphore.acquire();

        // TODO: connections 큐에서 Connection을 꺼내 반환하세요.
        //       (큐 접근은 synchronized로 보호하세요)
        synchronized (this){
            return connections.poll();
        }
    }

    public void release(Connection connection) {
        // TODO: connections 큐에 Connection을 다시 넣으세요.
        //       (큐 접근은 synchronized로 보호하세요)
        synchronized (this){
            connections.add(connection);
        }
        // TODO: Semaphore를 반환하세요. (퍼밋 반납)
        semaphore.release();
    }
}
