package org.example.producerconsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerMain {

    public static void main(String[] args) throws InterruptedException {
        int producerCount = 2;
        int consumerCount = 3;
        int itemsPerProducer = 5;
        int queueCapacity = 10;

        BlockingQueue<Item> queue = new ArrayBlockingQueue<>(queueCapacity);
        Item poisonPill = new Item(-1, "POISON");

        // TODO: Producer 스레드를 producerCount 만큼 생성하고 실행하세요.
        List<Thread> producers = new ArrayList<>();
        for(int i = 1; i<= producerCount; i++){
            Thread t = new Thread(new Producer(i, itemsPerProducer, queue));
            producers.add(t);
            t.start();
        }

        // TODO: Consumer 스레드를 consumerCount 만큼 생성하고 실행하세요.
        List<Thread> consumers = new ArrayList<>();
        for(int i = 1; i<= consumerCount; i++){
            Thread t = new Thread(new Consumer(i, queue, poisonPill));
            consumers.add(t);
            t.start();
        }

        // TODO: 모든 Producer가 끝난 후 Consumer 수만큼 poisonPill을 queue에 넣으세요.
        for (Thread producer : producers){
            producer.join();
        }
        for (int i = 0; i< consumerCount; i++){
            queue.put(poisonPill);
        }

        // TODO: 모든 스레드가 종료될 때까지 기다리세요. (join)
        for (Thread consumer : consumers){
            consumer.join();
        }

        System.out.println("모든 작업 완료");
    }
}
