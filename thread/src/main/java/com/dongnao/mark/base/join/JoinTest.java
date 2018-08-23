package com.dongnao.mark.base.join;

public class JoinTest {
    static class ThreadA extends Thread{
        private ThreadB threadB;
        public ThreadA(ThreadB threadB){
            this.threadB = threadB;
        }
        @Override
        public void run() {
            try {
                System.out.println("ThreadA 线程A 开始运行");

                System.out.println("ThreadA 线程A 结束运行");
                /**线程B插队，等线程B run()方法执行完，再执行线程A run()方法*/
                this.threadB.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class ThreadB extends Thread{
        @Override
        public void run() {
            System.out.println("ThreadB 线程B 开始运行");
            System.out.println("ThreadB 线程B 结束运行");
        }
    }

    public static void main(String[] args) {
        ThreadB threadB = new ThreadB();
        ThreadA threadA = new ThreadA(threadB);
        threadA.start();
        threadB.start();
    }
}
