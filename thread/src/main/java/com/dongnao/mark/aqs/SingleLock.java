package com.dongnao.mark.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SingleLock implements Lock {
    /**定义内部类 Sync继承AQS同步器 AbstractQueuedSynchronizer，至少实现 tryAcquire()，
     * tryRelease() 方法复写，以便被模版方法 acquire()调用*/
    static class Sync extends AbstractQueuedSynchronizer{

        /**tryAcquire(int arg) 获取独占锁方法，会被模版方法调用，args表示获取锁的数量，默认为 1*/
        @Override
        public boolean tryAcquire(int arg) {
            /**原子操作方法 compareAndSetState()，利用CAS原理，死循环去检查某个变量是否为期望的值，
             * 如果某个变量变成了期望的值，就是修改；此处表示如果 state变量值如果变为0，那么compareAndSetState
             * 就会将其修改为 1；*/
            if (this.compareAndSetState(0,1)){
                /*设置独占线程，独占线程即为当前线程*/
                this.setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        /**释放独占锁，args表示释放锁的数量，默认为 1*/
        @Override
        public boolean tryRelease(int arg) {
            /*设置独占线程为 null*/
            this.setExclusiveOwnerThread(null);
            /*state=0，表示释放锁*/
            this.setState(0);
            return false;
        }
        /**对象的锁是否被某个线程获取*/
        @Override
        protected boolean isHeldExclusively() {
            /*state = 1 说明有线程独占了锁*/
            return this.getState() == 1;
        }
        public Condition newCondition(){
            return new ConditionObject();
        }
    }

    private Sync sync = new Sync();

    @Override
    public void lock() {
        /**参数 1 表示当前锁为独占锁，同时只有一个线程能够获取到*/
        /**acquire()为模版方法，会固定调用几个未真正实现的方法，Sync类中重新的方法就是这几个未真正实现的方法*/
        sync.acquire(1);
    }
    /**获取锁，可中断*/
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }
    /**尝试非阻塞的获取锁*/
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }
    /**尝试非阻塞的获取锁，可超时*/
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
