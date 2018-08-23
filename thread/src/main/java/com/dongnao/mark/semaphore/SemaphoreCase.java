package com.dongnao.mark.semaphore;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaphoreCase<T> {/*容器特征：定义泛型*/
    private Semaphore items;/*有多少元素可拿*/
    private Semaphore spaces;/*有多少空位可放元素*/
    /*LinkedList线程不安全，因此对queue需要进行同步，sychronized(queue)*/
    private List<T> queue = new LinkedList<T>();

    public SemaphoreCase(int count) {/*最多可同时访问queue的线程数量*/
        this.items = new Semaphore(0);/*最开始，没有元素可以拿*/
        this.spaces = new Semaphore(count);/*刚开始，有count个空位可放元素*/
    }

    public void put(T t) throws InterruptedException {
        this.spaces.acquire(1);/*获取空位*/
        synchronized (this.queue){
            this.queue.add(t);
        }
        this.items.release(1);/**/
    }
    public T take() throws InterruptedException {
        this.items.acquire(1);
        T t;
        synchronized (this.queue){
            t = (T)this.queue.remove(0);
        }
        this.spaces.release(1);/*有一个空间释放出来了，释放掉*/
        return t;
    }
}
