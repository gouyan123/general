package com.dongnao.mark.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SharedLock implements Lock {
    static class Sync extends AbstractQueuedSynchronizer {
        Sync(int count){
            this.setState(count);/*设置锁的数量*/
        }
        /**共享锁的获取*/
        @Override
        protected int tryAcquireShared(int arg) {
            /**自旋*/
            for (;;){
                /**查看锁数量*/
                int current = this.getState();
                /**当前的锁的数量减去要拿的锁的数量等于剩余的锁的数量*/
                int newCurrent = current - arg;
                /**当前锁数量，拿完后剩余锁的数量，设置成功则返回true*/
                //this.compareAndSetState(current,newCurrent);
                if (newCurrent < 0 || this.compareAndSetState(current,newCurrent)){
                    return newCurrent;
                }
            }
        }
        /**共享锁的释放*/
        @Override
        protected boolean tryReleaseShared(int arg) {
            /**自旋*/
            for (;;){
                /**当前锁的数量*/
                int current = this.getState();
                /**当前的锁的数量加上释放的锁的数量*/
                int newCurrent = current + arg;
                /**当前锁数量，拿完后剩余锁的数量，设置成功则返回true*/
                //this.compareAndSetState(current,newCurrent);
                if (this.compareAndSetState(current,newCurrent)){
                    return true;
                }else {
                    return false;
                }
            }
        }
        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    final Sync sync = new Sync(3);

    @Override
    public void lock() {
        /**每个线程进来扣减一把锁*/
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1) > 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        /*每个线程结束，还回一把锁*/
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
