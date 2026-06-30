package org.example.connectionpool;

public class Worker implements Runnable {

    private final int id;
    private final ConnectionPool pool;

    public Worker(int id, ConnectionPool pool) {
        this.id = id;
        this.pool = pool;
    }

    @Override
    public void run() {
        // TODO: pool에서 Connection을 획득하세요.
        //       획득 시 "Worker [id] - 커넥션 획득: [connection]" 출력

        // TODO: connection.query()로 쿼리를 실행하세요. (sql 내용은 자유)

        // TODO: pool에 Connection을 반납하세요.
        //       반납 시 "Worker [id] - 커넥션 반납: [connection]" 출력

        // TODO: InterruptedException은 RuntimeException으로 감싸서 처리하세요.
        try {
            Connection connection = pool.acquire();
            System.out.println("Worker [" + id + "] - 커넥션 획득: [" + connection + "]");

            connection.query("");

            System.out.println("Worker [" + id + "] - 커넥션 반납: [" + connection + "]");
            pool.release(connection);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
