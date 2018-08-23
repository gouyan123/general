package com.dongnao.mark.mythreadpool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyThreadPool {
    private Integer work_num = 5;
    /**工作线程组：工作线程数组*/
    private WorkThread[] workThreads;
    /**任务队列：存放线程来不及执行的 任务线程对象*/
    private List<Runnable> taskQueue;
    public MyThreadPool(Integer work_num){
        this.work_num = work_num;
        this.workThreads = new WorkThread[this.work_num];
        /**存放任务线程对象*/
        this.taskQueue = new LinkedList<Runnable>();
        /**创建 WorkThread 线程，添加到WorkThread[]线程池中*/
        for (int i=0;i<this.work_num;i++){
            this.workThreads[i] = new WorkThread();
            /**启动线程*/
            this.workThreads[i].start();
        }
    }
    /**工作线程：不断去任务队列中拿任务*/
    private class WorkThread extends Thread{
        /**true：线程运行，false：线程终止*/
        private volatile boolean on = true;
        @Override
        public void run() {
            /**Runnable代表任务不是线程*/
            Runnable runnable = null;
            /**工作线程：不断去任务队列中拿任务线程对象，并执行获取的任务*/
            try {
                /**线程状态：运行*/
                while (this.on && !this.isInterrupted()){
                    /**多线程同时修改同一个共享变量，线程不安全，要对这个共享变量进行锁定，只有一个线程可以执行*/
                    synchronized (taskQueue){
                        while (this.on && !this.isInterrupted() && taskQueue.isEmpty()){
                            /**线程操作的对象可以让线程等待，或者唤醒线程*/
                            /**wait(1000)等待超时机制：等待 1s 后重新唤醒，去获取队列任务*/
                            taskQueue.wait(1000);
                        }
                        /**线程去任务队列获取任务*/
                        if (this.on && !this.isInterrupted() && !taskQueue.isEmpty()){
                            runnable = taskQueue.remove(0);
                        }
                    }
                    /**线程安全，不需要 synchronized锁定*/
                    /**执行任务队列里面的线程，任务队列里面存任务线程对象*/
                    if (runnable != null){
                        System.out.println(Thread.currentThread().getId() + " ready excute...");
                        /**执行任务线程*/
                        runnable.run();
                    }
                    /**释放runnable指向的堆内存空间*/
                    runnable = null;
                }
            }catch (InterruptedException e){
                System.out.println(Thread.currentThread().getId() + " is Interrupted");
            }
        }
        /**中断工作线程*/
        public void stopWorkThread(){
            this.on = false;
            /**interrupt()作用：①将中断标志位修改为 true；②让sleep(),wait()等方法抛出异常，当抛
             * 出异常时，中断标志位为变为false；sleep()，wait()方法死循环检查中断标志位，为ture则
             * 抛出异常；*/
            this.interrupt();
        }
    }
    public void execute(Runnable task){
        /**多个线程修改一个共享成员变量时，线程不安全，可以锁定这个共享成员变量，保证线程安全*/
        synchronized (this.taskQueue){
            this.taskQueue.add(task);
            /**线程启动以后，处于等待 wait 状态，要对其进行通知 notifyAll*/
            this.taskQueue.notifyAll();
        }
    }

    /**中断线程池*/
    public void destroy(){
        System.out.println("Ready stop ThreadPool!");
        /**对所有线程进行中断*/
        for (int i=0;i<this.work_num;i++){
            this.workThreads[i].stopWorkThread();
            /**加速垃圾回收*/
            workThreads[i] = null;
        }
        /**清理任务队列*/
        this.taskQueue.clear();
    }
}


