package com.dongnao.mark.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTemplate {
    public static void main(String[] args) {
        /**Reentrant可重入*/
        Lock lock = new ReentrantLock();
        lock.lock();/*获取锁，写在try块外面*/

        try{

        }finally {
            lock.unlock();/*释放锁，写在finally里面*/
        }
    }
}
