package com.dongnao.mark.delayqueue;

import java.util.concurrent.DelayQueue;

public class PutInCache implements Runnable {
    private DelayQueue<CacheBean<User>> queue;

    public PutInCache(DelayQueue<CacheBean<User>> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        CacheBean cacheBean = new CacheBean(1,"5s",new User("Mark"),5000l);
        CacheBean cacheBean2 = new CacheBean(2,"3s",new User("Mike"),3000l);
        this.queue.offer(cacheBean);
        System.out.println("put in cache :" + cacheBean.getId() + cacheBean.getName());
        this.queue.offer(cacheBean2);
        System.out.println("put in cache :" + cacheBean2.getId() + cacheBean2.getName());
    }
}
