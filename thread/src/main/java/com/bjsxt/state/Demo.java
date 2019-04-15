package com.bjsxt.state;

public class Demo {
    static class AThread implements Runnable{

        @Override
        public void run() {
        }
    }

    public static void main(String[] args) {
        AThread aThread = new AThread();
        Thread t = new Thread(aThread);
        System.out.println(t.getState());
        t.start();
        System.out.println(t.getState());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getState());
    }
}
