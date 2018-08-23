package com.dongnao.mark.lock.LimitedBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueLC<T> {
    private List<T> queue = new LinkedList<T>();
    private int limit;
    public BlockingQueueLC(int limit){
        this.limit = limit;
    }
    private Lock lock = new ReentrantLock();
    private Condition needNotEmpty = lock.newCondition();
    private Condition needNotFull = lock.newCondition();

    public void enqueue(T t) throws InterruptedException {
        lock.lock();
        try {
            //do my work
            while (this.queue.size() == this.limit){
                needNotFull.await();//需要不满，当前为满，线程等待
            }
            this.queue.add(t);
            needNotEmpty.signal();//当前不空，条件需要不空，通知线程执行；
        }finally {
            lock.unlock();
        }
    }

    public T dequeue() throws InterruptedException {
        lock.lock();
        try {
            //do my work...
            while (this.queue.size() == 0){
                needNotEmpty.await();//条件需要不空，当前为空，因此等待
            }
            T t = this.queue.remove(0);
            needNotFull.signal();//当前不满，条件需要不满，因此通知线程运行
            return t;

        }finally {
            lock.unlock();
        }
    }

}
