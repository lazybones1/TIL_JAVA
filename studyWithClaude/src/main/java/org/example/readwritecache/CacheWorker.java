package org.example.readwritecache;

public class CacheWorker implements Runnable {

    private final int id;
    private final Cache cache;
    private final CacheStats stats;
    private final int readCount;
    private final int writeCount;

    public CacheWorker(int id, Cache cache, CacheStats stats, int readCount, int writeCount) {
        this.id = id;
        this.cache = cache;
        this.stats = stats;
        this.readCount = readCount;
        this.writeCount = writeCount;
    }

    @Override
    public void run() {
        // TODO: writeCount 만큼 반복하며 cache에 값을 저장하세요.
        //       key는 "worker-[id]-[i]", value는 "value-[id]-[i]" 형식으로 하세요.
        //       저장 시 "Worker [id] PUT key=[key]" 출력
        for (int i = 1; i<= writeCount; i++){
            String key = "worker-["+id+"]-["+i+"]";
            String value = "value-["+id+"]-["+i+"]";
            cache.put(key, value );
            System.out.println("Worker ["+id+"] PUT key=["+key+"]");
        }

        // TODO: readCount 만큼 반복하며 cache에서 값을 읽으세요.
        //       key는 "worker-[id]-[i % writeCount]" 형식으로 하세요.
        //       읽은 값이 null이 아니면 stats.recordHit(), null이면 stats.recordMiss()를 호출하세요.
        //       읽기 시 "Worker [id] GET key=[key] -> [value]" 출력
        for (int i = 1; i<=readCount; i++){
            String key = "worker-["+id+"]-["+i % writeCount+"]";
            String value = cache.get(key);
            if (value == null ){
                stats.recordMiss();
            }else{
                stats.recordHit();
            }
            System.out.println("Worker ["+id+"] GET key=["+key+"] -> ["+value+"]");
        }
    }
}
