package com.dongnao.mark.base.connectpool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 从连接池中获取，使用，和释放连接的过程，客户端获取连接的过程被设定为等待超时的模式：
 * 如果1秒内无法获取到可用连接，将会返回给客户端一个null；
 * 设定连接池的大小为10，然后通过调节客户端的线程数来模拟无法获取连接的场景；
 * 连接池的定义：通过构造函数初始化连接的最大上限，通过一个双向队列来维护连接；
 * 调用方需要先调用fetchConnection(long)方法来指定，在多少毫秒内超时获取连接，当连接使用完成后，需要调用
 * releaseConnection(connection)方法将连接放回连接池；
 */
public class ConnectionPool {
    /**pool容器：存 数据库连接对象 connection*/
    private LinkedList<Connection> pool = new LinkedList<Connection>();//LinkedList双向list
    /**初始化 pool容器，即向容器 pool中添加数据库连接对象 connection*/
    public ConnectionPool(int initialSize) {
        if (initialSize > 0) {
            for (int i = 0; i < initialSize; i++) {
                pool.addLast(ConnectionDriver.getConnection());
            }
        }
    }

    /**将连接放回 连接池*/
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                /**添加后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个连接*/
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    /**指定在多少毫秒内获取连接超时，在指定时间内无法获取连接，将会返回null*/
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            //完全超时
            if (mills <= 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                long future = System.currentTimeMillis() + mills;/*超时的时刻*/
                long remaining = mills;/*超时的时长*/
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();/*当前还需等待的时间*/
                }
                Connection result = null;
                if (!pool.isEmpty()) {
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }
}
