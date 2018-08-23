package com.dongnao.mark.base.interrupt;

/**使用自定义的取消标志位中断线程（不靠谱）*/
public class FlagCancel {
    private static class TestRunable implements Runnable{
        private volatile boolean on = true;
        private long i =0;
        @Override
        public void run() {
            while(on){
                System.out.println(i++);
                /**当run()方法阻塞在 try{}块中wait()方法时，标志位 on 无法中断线程，因为执行不到
                 * 下一次循环了；
                 * 当run()方法中取消 wait()阻塞时，标志物 on 可以中断线程；*/
                try {
                    synchronized (this){
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("TestRunable is runing :"+i);
        }

        public void cancel(){
            System.out.println("Ready stop thread......");
            on = false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestRunable testRunable = new TestRunable();
        Thread t = new Thread(testRunable);
        t.start();
        Thread.sleep(1000);
        testRunable.cancel();
    }

}
