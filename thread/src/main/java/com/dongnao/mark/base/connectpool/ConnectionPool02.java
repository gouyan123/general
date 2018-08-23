package com.dongnao.mark.base.connectpool;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool02 {
    private volatile List<Connection> pool = new ArrayList<Connection>();
    private int poolSize;
    public ConnectionPool02(int poolSize) {
        for (int i=0;i<poolSize;i++){
            Connection connection = ConnectionDriver.getConnection();
            pool.add(connection);
        }
    }
    public Connection getConnection(long waitTime) throws InterruptedException {
        synchronized (pool){
            long current = System.currentTimeMillis();
            long future = current + waitTime;
            /**等待通知机制*/
            /**不满足条件则等待*/
            while (this.pool.size() == 0){
                /**wait(long timeout)超时被唤醒；future-d当前时刻 = 剩余等待时间*/
                this.pool.wait(future-System.currentTimeMillis());
                return null;
            }
            /**满足条件则执行逻辑*/
            Connection connection = this.pool.remove(0);
            /**满足条件则通知其他等待线程*/
            if (this.pool.size() < this.poolSize){
                this.pool.notifyAll();
            }
            return connection;
        }
    }
    public void releaseConnection(Connection connection) throws InterruptedException {
        /**其他线程不能获得锁，要使用 wait()释放锁，原地等待唤醒，再判断条件是否合适；*/
        synchronized (this.pool){
            while (this.pool.size() == this.poolSize){
                this.pool.wait();
            }
            this.pool.add(connection);
            if (this.pool.size() > 0){
                this.pool.notifyAll();
            }
        }
    }


}
