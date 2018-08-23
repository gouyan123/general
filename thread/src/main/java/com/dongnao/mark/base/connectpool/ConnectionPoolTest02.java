package com.dongnao.mark.base.connectpool;

import com.alibaba.fastjson.JSON;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;

public class ConnectionPoolTest02 {
    static class ConnectionThread implements Runnable{
        private ConnectionPool02 pool;
        private CountDownLatch latch;
        public ConnectionThread(ConnectionPool02 pool) {
            this.pool = pool;
        }
        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }
        @Override
        public void run() {
            try {
                Connection connection = this.pool.getConnection(10000);

                System.out.println("ThreadId " + Thread.currentThread().getId() + " " + JSON.toJSONString(connection));
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class ReleaseThread implements Runnable{
        private ConnectionPool02 pool;
        private Connection connection;
        public ReleaseThread(ConnectionPool02 pool, Connection connection) {
            this.pool = pool;
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                this.pool.releaseConnection(this.connection);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);
        ConnectionPool02 pool02 = new ConnectionPool02(1);
        long start = System.currentTimeMillis();
        System.out.println("开始时间 = " + start);
        ConnectionThread connectionThread = new ConnectionThread(pool02);
        connectionThread.setLatch(latch);
        new Thread(connectionThread).start();
        new Thread(connectionThread).start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("结束时间 = " + end);
        System.out.println("等待时间 = " + (end - start));
    }
}
