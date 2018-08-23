package com.dongnao.mark.mythreadpool;

public class TestMyThreadPool {
    private static class TaskThread implements Runnable{
        private String name;
        public TaskThread(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        @Override
        public void run() {
            System.out.println(this.name + " is running!");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.name + " finish!");
        }
    }
    public static void main(String[] args) throws InterruptedException {
        /**初始化线程池，工作线程进入等待模式，不断检查任务队列中是否有任务线程，有则获取并执行*/
        MyThreadPool myThreadPool = new MyThreadPool(5);
        /**定义任务线程类*/
        /**线程池将任务线程对象添加到任务队列成员变量中，并通知线程执行*/
        myThreadPool.execute(new TaskThread("A任务线程"));
        myThreadPool.execute(new TaskThread("B任务线程"));
        Thread.sleep(6000);
        myThreadPool.destroy();
    }
}
