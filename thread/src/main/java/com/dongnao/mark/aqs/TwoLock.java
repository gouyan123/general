package com.dongnao.mark.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TwoLock implements Lock {

    static class Sync extends AbstractQueuedSynchronizer {
        /**Sync构造方法，设置锁个数*/
        Sync(int count){
            setState(count);
        }

        /**共享锁获取，args表示获取锁的数量*/
        public int tryAcquireShared  (int arg){
            for(;;){
                /**看还剩几个锁可以拿 */
                int current = getState();
                /**锁的总数量 - 要走的锁的数量 = 剩余锁的数量*/
                int newCount = current - arg;
                if(newCount<0||compareAndSetState(current,newCount)){
                    return newCount;
                }
            }
        }

        //共享锁锁释放
        public boolean tryReleaseShared  (int arg){
            for(;;){
                int current = getState();
                int newCount = current + arg;
                if(compareAndSetState(current,newCount)){
                    return true;
                }
            }
        }

        Condition newCondition(){
            return new ConditionObject();
        }

    }

    private final Sync sync = new Sync(2);

    @Override
    public void lock() {
        /**acquireShared(1)为模版方法会调用里面的 tryAcquireShared(int arg)方法*/
        sync.acquireShared(1);

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);

    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1)>=0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);

    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
