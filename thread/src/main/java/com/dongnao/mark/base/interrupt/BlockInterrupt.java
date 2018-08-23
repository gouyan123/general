package com.dongnao.mark.base.interrupt;

/**BlockInterrupt类里面定义 2 个线程类：
 * ①WhileTryWhenBlock：run()方法中 try...catch块写在 while()循环里面；catch块中定义
 * Thread.currentThread().interrupt()，用于设置线程中断标志位；
 * 执行流程：main线程先执行whileTryWhenBlock.start();然后 whileTryWhenBlock线程阻塞在 run()中
 * wait()方法处，main线程休眠后，执行 whileTryWhenBlock.cancel();cancel()方法里面调用 interupt()
 * 方法，这个方法会使 run()方法中的阻塞方法抛出异常；run()方法进入catch()块
 * ②TryWhileWhenBlock：run()方法中 while()写在 try...catch块循环里面*/
public class BlockInterrupt {
    private static Object o = new Object();
    /*while循环中包含try/catch块*/
    private static class WhileTryWhenBlock extends Thread {
        private volatile boolean on = true;
        private long i =0;
        @Override
        public void run() {
            System.out.println("当前执行线程id："+Thread.currentThread().getId());
            while (on && !Thread.currentThread().isInterrupted()) {
                System.out.println("i="+i++);
                try {
                    synchronized (o){
                        /**线程会停在这里，直到 main线程中调用 whileTryWhenBlock.cancel();
                         * cancel()方法里面调用 interrupt()方法后，中断标志位会改成true，
                         * run()中阻塞方法检测到阻塞线程中断标志位为true时，会抛出中断异常，且中
                         * 断标志位改为 false；*/
                        o.wait();
                        /**wait()方法实质：循环检查阻塞在这里线程的中断标志位，当中断标志位为true
                         * 时，wait()方法抛出异常，且将中断标志位改为 false*/
                    }

                } catch (InterruptedException e) {
                    /**调用线程的中断方法 interrupt()方法时，阻塞方法wait()会抛出异常，因此进入
                     * catch块*/
                    System.out.println("当前执行线程的中断标志位："
                            +Thread.currentThread().getId()
                            /**isInterrupted()取出线程中断标志位*/
                            +":"+Thread.currentThread().isInterrupted());
                    /**将中断标志位改为 true；*/
                    Thread.currentThread().interrupt();
                    /**每个线程都有自己的 id号，getId()取得这个 id号*/
                    System.out.println("被中断的线程_"+super.getId()
                            +":"+isInterrupted());
                    //do my work
                }
                //清理工作，准备结束线程
            }
        }

        public void cancel() {
            /**interrupt()方法：
             * ①将中断标志位改为 true；
             * ②会使阻塞方法抛出中断异常 InterruptedException，且将中断标志位改为 false；*/
            super.interrupt();
            /**每个线程类都有一个自己的 id号，getId()就是这个 id号；*/
            System.out.println("本方法所在线程实例："+super.getId());
            /**Thread.currentThread().getId()：当前执行的线程的id，此处，当前线程是 main线程*/
            System.out.println("执行本方法的线程："+Thread.currentThread().getId());
            //Thread.currentThread().interrupt();
        }
    }

    /*try/catch块中包含while循环*/
    private static class TryWhileWhenBlock  extends Thread {
        private volatile boolean on = true;
        private long i =0;

        @Override
        public void run() {
            try {
                while (on) {
                    System.out.println(i++);
                    //抛出中断异常的阻塞方法，抛出异常后，中断标志位改成false
                    synchronized (o){
                        o.wait();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("当前执行线程的中断标志位："
                        +Thread.currentThread().getId()
                        +":"+Thread.currentThread().isInterrupted());
            }finally {
                //清理工作结束线程
            }
        }
        public void cancel() {
            on = false;
            interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WhileTryWhenBlock whileTryWhenBlock = new WhileTryWhenBlock();
        whileTryWhenBlock.start();
        Thread.sleep(1000);
        /**执行 cancel()方法调用的是 main()线程*/
        whileTryWhenBlock.cancel();
        /*TryWhileWhenBlock tryWhileWhenBlock = new TryWhileWhenBlock();
        tryWhileWhenBlock.start();
        Thread.sleep(100);
        tryWhileWhenBlock.cancel();*/
    }
}
