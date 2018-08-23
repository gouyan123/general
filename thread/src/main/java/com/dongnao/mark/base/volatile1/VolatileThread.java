package com.dongnao.mark.base.volatile1;

public class VolatileThread implements Runnable{
    private volatile int a = 0;
    @Override
    public void run() {
        this.a++;
        System.out.println("F Thread name : " + Thread.currentThread().getName() + ";a = " + this.a);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.a++;
        System.out.println("S Thread name : " + Thread.currentThread().getName() + ";a = " + this.a);
    }

    public static void main(String[] args) {
        VolatileThread volatileThread = new VolatileThread();
        for (int i=0;i<5;i++){
            new Thread(volatileThread).start();
        }
    }
}
