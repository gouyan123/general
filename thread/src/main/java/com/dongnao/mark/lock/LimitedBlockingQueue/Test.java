package com.dongnao.mark.lock.LimitedBlockingQueue;

public class Test {
    static class Producer implements Runnable{
        private BlockingQueueLC02 blockingQueueLC;
        public Producer(BlockingQueueLC02 b){
            this.blockingQueueLC = b;
        }
        private int x = 0;
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                this.blockingQueueLC.enqueue(this.x);
                System.out.println("线程 "+Thread.currentThread().getId()+"添加 x = "+x);
                x++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class Consumer implements Runnable{
        private BlockingQueueLC02 blockingQueueLC;
        public Consumer(BlockingQueueLC02 b){
            this.blockingQueueLC = b;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                Object x = this.blockingQueueLC.dequeue();
                System.out.println("线程 "+Thread.currentThread().getId()+"取出 x = "+x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueueLC02 b = new BlockingQueueLC02(3);
        Producer p = new Producer(b);
        Consumer c = new Consumer(b);
        for (int i=0;i<5;i++){
            new Thread(p).start();
        }
        for (int i=0;i<1;i++){
            new Thread(c).start();
        }
    }
}
