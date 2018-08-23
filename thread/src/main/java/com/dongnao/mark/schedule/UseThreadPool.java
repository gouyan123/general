package com.dongnao.mark.schedule;

import java.util.Random;
import java.util.concurrent.*;

public class UseThreadPool {
    /*定义线程任务类，线程任务对象提交给线程池*/
    private static class MyTask implements Runnable{
        private String name;
        public MyTask(String name){
            this.name = name;
        }
        public String getName() {
            return name;
        }
        @Override
        public void run() {
            System.out.println(this.name + " is Running!");
            try {
                Thread.sleep(1000 + new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.name + " is Finished!");
        }
    }

    public static void main(String[] args) {
        /*corePoolSize:核心线程数，线程池中实际线程数小于核心线程数时，对于新提交任务，直接创建线程执行；
        * 如果线程池中实际线程数等于核心线程数时，对于新提交任务，保存到任务队列；
        * 如果任务队列已满，创建新线程执行该任务，但是总线程数不能超过最大线程数maximumPoolSize；
        * maximumPoolSize：最大线程数
        * keepAliveTime：线程如果一直空闲，过60秒就被干掉
        * new ArrayBlockingQueue<Running>(10)：线程任务类对象的存储队列
        * */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,4,
                60, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
        /*线程池中提前创建好线程，线程数固定为n，任务队列为无界队列，当任务数超过现有线程数时，在队列中等待*/
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        /*线程池中提前创建好线程，线程数固定为1，任务队列为无界队列，当任务数超过现有线程数时，在队列中等待*/
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        /*corePoolSize=0说明线程池不提前创建好线程，maximumPoolSize=Integer.MAX_VALUE说明线程池会不断创建新线程
        * 来处理任务 keepAliveTime=60L说明线程池中线程空闲60s就会被干掉；
        * */
        ExecutorService cacheThreadExecutor = Executors.newCachedThreadPool();
        ExecutorService workSteelThreadExecutor = Executors.newWorkStealingPool();
        for (int i=0;i<5;i++){
            /*创建线程任务类对象*/
            MyTask myTask = new MyTask("Thread_" + i);
            System.out.println("A Task will add:" + myTask.getName());
            /*将线程任务类对象保存到任务队列中，任务队列通知工作线程去任务队列中取线程任务类对象；
            * 获取到线程任务类对象后，调用其run()方法；
            * */
            threadPoolExecutor.execute(myTask);
        }
        threadPoolExecutor.shutdown();
    }
}
