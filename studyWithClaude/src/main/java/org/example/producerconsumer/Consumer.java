package org.example.producerconsumer;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final int id;
    private final BlockingQueue<Item> queue;
    private final Item poisonPill;

    public Consumer(int id, BlockingQueue<Item> queue, Item poisonPill) {
        this.id = id;
        this.queue = queue;
        this.poisonPill = poisonPill;
    }

    @Override
    public void run() {
        // TODO: queue에서 Item을 꺼내 처리하세요.
        //       queue.take() 사용 (비어 있으면 대기)
        //       꺼낸 Item이 poisonPill이면 종료하세요.
        //       처리 시 어떤 Consumer가 어떤 Item을 처리했는지 출력하세요.
        while (true){
            try {
                Item item = queue.take();
                if (item == poisonPill) break;
                System.out.println("Consumer " + id + "가 " + item.getId() + "처리");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
