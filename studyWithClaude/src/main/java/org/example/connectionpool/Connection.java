package org.example.connectionpool;

public class Connection {
    private final int id;

    public Connection(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void query(String sql) throws InterruptedException {
        // 쿼리 실행을 시뮬레이션 (100ms 대기)
        Thread.sleep(100);
    }

    @Override
    public String toString() {
        return "Connection{id=" + id + "}";
    }
}
