package com.dongnao.mark.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RwLockTemplate  {
    static final Map<String,String> map = new HashMap<String,String>();
    static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    /*获取读锁*/
    static Lock readLock = reentrantReadWriteLock.readLock();
    /*获取写锁*/
    static Lock writeLock = reentrantReadWriteLock.writeLock();

    public void get(){
        readLock.lock();
        try{
            //do my work...
        }finally {
            readLock.unlock();
        }
    }

    public void put(){
        writeLock.lock();
        try {
            //do my work...
        }finally {
            writeLock.unlock();
        }
    }
}
