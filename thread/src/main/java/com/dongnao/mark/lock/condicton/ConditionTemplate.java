package com.dongnao.mark.lock.condicton;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTemplate {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void waitc(){
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void waitnotify(){
        lock.lock();
        try {
            condition.signal();
            //condition.signalAll();尽量少用signalAll()
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {

    }
}
