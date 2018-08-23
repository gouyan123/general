package com.dongnao.mark.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Test {
    static CyclicBarrier c = new CyclicBarrier(2);
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId() + " is waiting");
                try {
                    c.await();/*等在这里，等待数加1，直到等待数为2，继续运行*/

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getId() + " go on");
            }
        }).start();
        Thread.sleep(2000);
        System.out.println("main " + "is waiting");
        c.await();/*等在这里，等待数加1，直到等待数为2，继续运行*/
        System.out.println("main " + "go on");
    }
}
