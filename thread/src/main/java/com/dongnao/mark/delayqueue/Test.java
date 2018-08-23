package com.dongnao.mark.delayqueue;

import java.util.concurrent.DelayQueue;

public class Test {
    public static void main(String[] args) {
        DelayQueue<CacheBean<User>> queue = new DelayQueue<CacheBean<User>>();
        new Thread(new PutInCache(queue)).start();
        new Thread(new GetFromCache(queue)).start();
    }
}
