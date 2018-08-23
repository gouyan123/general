package com.dongnao.mark.base.threadlocal;

public class ThreadLocalTest {
    /**创建 ThreadLocal对象*/
    static ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
        /**initialValue()给<String>赋初始值*/
        @Override
        protected String initialValue() {
            return "init";
        }
    };
    private static class T1 implements Runnable{
        private int id;
        public T1(int id) {
            this.id = id;
        }
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getId()+" start");
            /**threadLocal.get()只能写在线程run()方法里面，因为threadLocal要取当前线程作为 key*/
            String s = threadLocal.get();
            s = s+"_"+id;
            /**threadLocal.set()只能写在线程run()方法里面，因为threadLocal要取当前线程作为 key*/
            threadLocal.set(s);
            System.out.println(Thread.currentThread().getId() + " " + threadLocal.get());
        }
    }
    public void test(){
        Thread[] runs = new Thread[3];
        for(int i =0;i<runs.length;i++){
            runs[i]=new Thread(new T1(i));
        }
        for(int i =0;i<runs.length;i++){
            runs[i].start();
        }
    }
    static ThreadLocal<Integer> threadLocal02 = new ThreadLocal<Integer>();
    static class MyThreadLocal implements Runnable{

        @Override
        public void run() {
            /***/
            threadLocal02.set(1);
        }
    }

    public static void main(String[] args) {
        ThreadLocalTest test = new ThreadLocalTest();
        test.test();
    }
}
