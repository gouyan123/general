package com.dongnao.mark.base.interrupt;

public class InterruptWaitingDemo extends Thread {
    @Override
    public void run() {
        System.out.println("id : " + Thread.currentThread().getId() + " state " +
                this.isInterrupted());
        int i = 0;
        while (!this.isInterrupted()) {
            try {
                // 模拟任务代码
                System.out.println("进入while循环" + i++);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // ... 清理操作
                // 重设中断标志位

                System.out.println("前 id : " + Thread.currentThread().getId() + " state " +
                Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt();
                System.out.println("后 id : " + Thread.currentThread().getId() + " state " +
                Thread.currentThread().isInterrupted());
            }
        }
        System.out.println(this.isInterrupted());/*InterruptWaitingDemo这个线程类*/
    }

    public static void main(String[] args) {
        InterruptWaitingDemo thread = new InterruptWaitingDemo();
        thread.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        /*对线程调用 interrupt 方法，wait/sleep/等方法会抛出异常*/
        thread.interrupt();
    }
}