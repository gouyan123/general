package com.dongnao.mark.delayqueue;

import java.util.concurrent.DelayQueue;

public class GetFromCache implements Runnable {
    private DelayQueue<CacheBean<User>> queue;

    public GetFromCache(DelayQueue<CacheBean<User>> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true){/*阻塞队列，因此，死循环它*/
            try {
                CacheBean<User> item = this.queue.take();
                System.out.println("GetFromCache" + item.getId() + item.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
