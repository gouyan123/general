package com.dongnao.mark.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class Test {
    /*计数器值为2，每个线程执行完毕时调用countDown()，计数器减1，计数器为0，
    * 解除await()的阻塞；
    * */
    private static CountDownLatch latch = new CountDownLatch(2);

    static Thread t1 = new Thread(){
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getId() + " 正在运行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getId() + " 运行完毕");
            /*线程运行完，执行coutDown()方法，计数器count值减1，当count=0时
            * 解除 latch.await()对主线程的阻塞；
            * */
            latch.countDown();
        }
    };
    static Thread t2 = new Thread(){
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getId() + " 正在运行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getId() + " 运行完毕");
            /*线程运行完，执行coutDown()方法，计数器count值减1，当count=0时
             * 解除 latch.await()对主线程的阻塞；
             * */
            latch.countDown();
        }
    };

    public static void main(String[] args) {
        t1.start();
        t2.start();
        System.out.println("等待 2 个子线程执行完毕");
        try {
            latch.await();/*当计数器为0时，解除主线程阻塞*/
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("2个线程运行结束，主线程解除阻塞");
    }
}
