package org.example.producerconsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

    private final int id;
    private final int itemCount;
    private final BlockingQueue<Item> queue;
    private static final AtomicInteger instanceCount = new AtomicInteger(0);

    public Producer(int id, int itemCount, BlockingQueue<Item> queue) {
        this.id = id;
        this.itemCount = itemCount;
        this.queue = queue;
    }

    @Override
    public void run() {
        // TODO: itemCount 개수만큼 Item을 생성하여 queue에 넣으세요.
        //       각 Item의 id는 고유하게, data는 자유롭게 설정하세요.
        //       queue.put() 사용 (가득 차면 대기)
        //       작업 완료 후 어떤 Producer가 몇 개를 생산했는지 출력하세요.
        int produceCount = 0;
        for (int i = 1; i<=itemCount; i++){
            try {
                queue.put(new Item(ItemIdCounter.next() , Integer.toString(i)));
                produceCount++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Producer " + this.id + "가 " + produceCount + "개");
    }
}
