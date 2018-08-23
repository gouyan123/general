package com.dongnao.mark.lock.LimitedBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueLC02<T> {
    private List<T> queue = new LinkedList<T>();
    private int limit;
    public BlockingQueueLC02(int limit){
        this.limit = limit;
    }
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void enqueue(T t) throws InterruptedException {
        lock.lock();
        try {
            while (this.limit == this.queue.size()){
                condition.await();
            }
            this.queue.add(t);
            /**唤醒等在这个对象上的其他线程，去争取锁*/
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
    public T dequeue() throws InterruptedException {
        lock.lock();
        try{
            while (this.queue.size() ==0){
                condition.await();
            }
            T t = this.queue.remove(0);
            condition.signal();
            return t;
        }  finally {
            lock.unlock();
        }
    }
}
