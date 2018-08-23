package com.dongnao.mark.base;

public class HowStartThread {
    private static class TestThread extends Thread{
        @Override
        public void run() {
            System.out.println("TestThread is Running");
        }
    }
    private static class TestRunnable implements Runnable{
        @Override
        public void run() {
            System.out.println("TestRunnable is Running");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new TestThread();
        Thread t2 = new Thread(new TestRunnable());//外面包一层 Thread，因为只有Thread类里有start方法
        t1.start();//启动线程
        t2.start();//启动线程
        System.out.println("main is Running");
    }
}
