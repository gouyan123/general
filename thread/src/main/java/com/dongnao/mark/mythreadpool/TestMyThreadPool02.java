package com.dongnao.mark.mythreadpool;

public class TestMyThreadPool02 {
    static class Task implements Runnable{
        private String name;
        public Task(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            System.out.println(name + " 执行！");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThreadPool02 pool02 = new MyThreadPool02(1);
        Runnable task1 = new Task("线程 A");
        Runnable task2 = new Task("线程 B");
        pool02.execute(task1);
        pool02.execute(task2);
        Thread.sleep(2000);
        pool02.destroy();
    }
}
