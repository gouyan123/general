package com.dongnao.mark.base.interrupt;

public class SafeInterrupt implements Runnable{
    /**设置线程共享变量，自己内存位置存储，公共内存位置也存储*/
    private volatile boolean flag = true;
    private Long i = 0l;
    @Override
    public void run() {
        System.out.println("thread运行状态:" + this.flag);
        while (this.flag  && !Thread.currentThread().isInterrupted()){
            this.i++;
/*                            try {
                    阻塞方法,flag就不起作用了
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
        }
        System.out.println("TestRunnable is Running : " + this.i);
    }
    public void cancel(){
        this.flag = false;
        Thread.currentThread().interrupt();
    }
    public static void main(String[] args) throws Exception {
        SafeInterrupt safeInterrupt = new SafeInterrupt();
        Thread t1 = new Thread(safeInterrupt);
        t1.start();
        Thread.sleep(3000);
        safeInterrupt.cancel();
        Thread.sleep(1000);
    }
}
