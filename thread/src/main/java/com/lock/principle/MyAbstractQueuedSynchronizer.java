package com.lock.principle;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**1.创建一个继承AbstractQueuedSynchronizer类的MyQueuedSynchronizer类*/
public class MyAbstractQueuedSynchronizer extends AbstractQueuedSynchronizer {
    /**2.声明一个私有的、AtomicInteger类型的属性state*/
    private AtomicInteger state;
    /**3.实现这个类的构造器，并初始化它的属性*/
    public MyAbstractQueuedSynchronizer() {
        this.state = new AtomicInteger(0);
    }
    /**4.实现tryAcquire()方法，该方法试图将变量state的值从0变成1；如果成功，它将返回true，否则，
     * 返回false*/
    @Override
    protected boolean tryAcquire(int arg) {
        return state.compareAndSet(0, 1);
    }
    /**5.实现tryRelease()方法，该方法试图将变量sate的值从1变成0.如果成功，它将返回true，否则，返回
     * false*/
    @Override
    protected boolean tryRelease(int arg) {
        return state.compareAndSet(1, 0);
    }
    /**6.创建一个MyLock类，并指定它实现Lock接口*/
    class MyLock implements Lock {
        /**7.声明一个私有的、AbstractQueuedSynchronizer类型的属性sync*/
        private AbstractQueuedSynchronizer sync;
        /**8.实现这个类的构造器，并使用MyAbstractQueueSynchronizer对象来初始化它的sync属性*/

        @Override
        public void lock() {

        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {

        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}
