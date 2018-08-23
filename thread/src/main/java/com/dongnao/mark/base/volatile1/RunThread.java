package com.dongnao.mark.base.volatile1;

public class RunThread extends Thread {
    /**isRunning前面不加 volatile ，main()方法会陷入死循环；*/
    private volatile boolean isRunning = true;
    public boolean isRunning() {
        return this.isRunning;
    }
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    @Override
    public void run() {
        System.out.println("进入到run方法中了");
        while (isRunning == true) {
        }
        System.out.println("线程执行完成了");
    }
    public static void main(String[] args) {
        try {
            RunThread thread = new RunThread();
            thread.start();
            Thread.sleep(1000);
            thread.setRunning(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}