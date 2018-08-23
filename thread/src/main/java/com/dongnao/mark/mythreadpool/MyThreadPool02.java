package com.dongnao.mark.mythreadpool;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool02 {
    /**定义任务列表，存Runnable任务*/
    private LinkedList<Runnable> tasks = new LinkedList<Runnable>();
    /**定义execute方法，向任务列表 tasks中存任务*/
    public void execute(Runnable task){
        /**多个线程操作 tasks，线程不安全，因此采用同步操作*/
        synchronized (tasks) {
            tasks.add(task);
            /**线程进入 synchronized块，说明已获得锁，其他线程有可能在 tasks对象上面等待，因此，
             * 唤醒等待在 tasks对象上面的线程*/
            tasks.notifyAll();
        }
    }
    /**定义一个工作线程，轮询任务队列，任务队列中有任务则取出，并执行其 run()方法*/
    private class WorkThread extends Thread{
        /**中断标志位*/
        private volatile boolean on = true;
        Runnable task = null;
        @Override
        public void run() {
            /**不中断线程，则一直轮询任务列表 tasks*/
            while (on==true && !isInterrupted()){
                synchronized (tasks){
                    /**等待通知机制*/
                    /**条件不满足则等待，此处列表为空，不能取出元素，则等待*/
                    while (on==true && !isInterrupted() && tasks.isEmpty()){
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                    /**条件满足则执行逻辑处理*/
                    if (!tasks.isEmpty()){
                        task = tasks.remove(0);
                        if (task != null){
                            task.run();
                        }
                    }
                    /**满足条件则通知等待在该对象上面的线程*/
                    tasks.notifyAll();
                }
            }
        }
        /**定义工作线程中断方法*/
        public void stopWorkThread(){
            on = false;
            interrupt();
        }
    }
    /**定义工作线程数组，保存工作线程对象*/
    private WorkThread[] workThreads;
    private int poolSize;
    /**构造方法，初始化线程池，并启动各线程*/
    public MyThreadPool02(int poolSize) {
        this.poolSize = poolSize;
        workThreads = new WorkThread[poolSize];
        for (int i=0;i<poolSize;i++){
            workThreads[i] = new WorkThread();
            workThreads[i].start();
        }
    }
    /**中断线程池*/
    public void destroy(){
        for (int i=0;i<poolSize;i++){
            this.workThreads[i].stopWorkThread();
            /**加速垃圾回收*/
            workThreads[i] = null;
        }
    }
}
