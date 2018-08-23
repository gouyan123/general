package com.dongnao.mark.semaphore;

import java.util.concurrent.Semaphore;

public class Test {
    static Semaphore semp = new Semaphore(5);
    static class SemaphoreThread implements Runnable{
        @Override
        public void run() {
            try {
                semp.acquire();/*获取共享锁，大小为5*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getId() + " is running");
            semp.release();/*获取共享锁，大小为5*/
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SemaphoreThread st = new SemaphoreThread();
        for (int i=0;i<8;i++){
            new Thread(st).start();
        }
        Thread.sleep(8);
        System.out.println("main");
    }
}
