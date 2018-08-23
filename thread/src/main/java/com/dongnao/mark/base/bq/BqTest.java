package com.dongnao.mark.base.bq;

import com.alibaba.fastjson.JSON;

import java.util.Random;

public class BqTest {
    static class Producer implements Runnable{
        private BlockingQueueWN<Integer> blockingQueueWN;
        public Producer(BlockingQueueWN<Integer> blockingQueueWN) {
            this.blockingQueueWN = blockingQueueWN;
        }
        @Override
        public void run() {
            try {
                Random random = new Random();
                int t = random.nextInt(10);
                this.blockingQueueWN.put(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class Consumer implements Runnable{
        private BlockingQueueWN<Integer> blockingQueueWN;
        public Consumer(BlockingQueueWN<Integer> blockingQueueWN) {
            this.blockingQueueWN = blockingQueueWN;
        }
        @Override
        public void run() {
            try {
                Integer t = this.blockingQueueWN.get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueueWN<Integer> blockingQueueWN = new BlockingQueueWN<Integer>(3);
        Producer producer = new Producer(blockingQueueWN);
        Consumer consumer = new Consumer(blockingQueueWN);
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(consumer).start();
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();
    }
}
