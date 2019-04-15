package com.dongnao.mark.base.threadstate;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**直接运行 main() 方法，在运行结果 4:Run 里面点击 相机查看线程 DUMP*/
public class ThreadState {
    private static Lock lock = new ReentrantLock();
    public static void main(String[] args) {
        /**DUMP显示线程状态：java.lang.Thread.State: TIMED_WAITING (sleeping)*/
        new Thread(new SleepAlways(), "SleepAlwaysThread").start();
        /**DUMP显示线程状态：java.lang.Thread.State: WAITING (on object monitor)*/
        new Thread(new Waiting(), "WaitingThread").start();
        /*BlockedThread-1，BlockedThread-2，一个获取锁成功，另一个被阻塞*/
        /**DUMP显示线程状态：java.lang.Thread.State: TIMED_WAITING (sleeping)
         *          - locked <0x00000000ec4f73c8>*/
        new Thread(new Blocked(), "BlockedThread-1").start();
        /**DUMP显示线程状态：java.lang.Thread.State: BLOCKED (on object monitor)
         * - waiting to lock <0x00000000ec4f73c8> 2阻塞在 *3c8锁，这个锁被 1拿到了*/
        new Thread(new Blocked(), "BlockedThread-2").start();
        /**DUMP显示线程状态：java.lang.Thread.State: WAITING (parking)
         * - parking to wait for  <0x00000000ec4ede30>*/
        new Thread(new Sync(), "SyncThread-1").start();
        /**DUMP显示线程状态：java.lang.Thread.State: TIMED_WAITING (sleeping)*/
        new Thread(new Sync(), "SyncThread-2").start();
    }
    /**该线程不断的进行睡眠*/
    static class SleepAlways implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(1000);
            }
        }
    }
    /**该线程在Waiting.class实例上等待*/
    static class Waiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    /**该线程在Blocked.class实例上加锁后，不会释放该锁*/
    static class Blocked implements Runnable {
        public void run() {
            /**加锁*/
            synchronized (Blocked.class) {
                while (true) {
                    SleepUtils.second(1000);
                }
            }
        }
    }
    /**该线程获得锁休眠后，又释放锁*/
    static class Sync implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                SleepUtils.second(3000);
            } finally {
                lock.unlock();
            }
        }
    }
}
