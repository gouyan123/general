package com.dongnao.mark.base.bq;

import java.util.LinkedList;
import java.util.List;
/**定义有界阻塞队列：
 * 有界：队列长度固定；阻塞：当条件不符时 wait()释放锁，在原地等待唤醒，试图争取锁，取得锁则执行，如条
 * 件扔不符合，就wait()等待，如条件符合，则执行业务逻辑，并notifyAll()其他线程*/
public class BlockingQueueWN<T> {
    /**定义 queue成员变量 用于存储数据*/
    private List<T> queue = new LinkedList<T>();
    /**定义 limit成员变量 表示*/
    private final int limit;

    public BlockingQueueWN(int limit) {
        this.limit = limit;
    }
    /**假设 3个线程来 存数据即调用 put(T t)方法，另 3个线程来 取数据及调用 get()，6个线程操作同一个
     * 对象 blockingQueueWN，blockingQueueWN对象的 put()方法 get()方法都上锁了，因此同一时刻，
     * 只有一个线程能获取到blockingQueueWN对象的锁，如果当前条件不符(如一个线程来存数据，但是列表已满)，
     * 当前线程必须使用 wait()释放锁并等待通知，继续尝试获取锁，继续判断条件是否符合，符合才运行，并notify
     * 唤醒其他线程，如果条件不符合还继续运行，而不使用 wait()释放锁，线程run()方法执行完毕，则彻底释放锁
     * ，没有再执行的机会，结果该线程存数据失败*/
    public synchronized boolean put(T t) throws InterruptedException {
        /*当前线程操作条件不符则 wait()释放锁等待，等待唤醒，再试图获取锁执行*/
        while (this.queue.size() == this.limit){
            this.wait();
        }

        this.queue.add(t);
        /**当前线程进入synchronized，说明当前线程获取到锁，其他线程有可能在 wait()处等待，因此需要
         * 唤醒其他线程，去争取锁*/
        if (this.queue.size() > 0){
            this.notifyAll();
        }
        System.out.println("ThreadId " + Thread.currentThread().getId() + " 添加数据 " + t + " size " + this.queue.size());
        return true;
    }
    public synchronized T get() throws InterruptedException {
        while (this.queue.size() == 0){
            this.wait();
        }

        T t = this.queue.remove(0);
        if (this.queue.size() < limit){
            this.notifyAll();
        }
        System.out.println("ThreadId " + Thread.currentThread().getId() + " 获取数据 " + t + " size " + this.queue.size());
        return t;
    }
}
