package cn.mldn.zookeeper;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class ZooKeeperWatcher {
    /**根节点路径*/
    private static final String GROUPNODE = "/mldndata";
    /**子节点路径*/
    private static final String SUBNODE = GROUPNODE + "/bigdata";
    private static ZooKeeper zooKeeper;
    public static void main(String[] args) throws Exception {

        /**连接地址，多个连接地址使用 , 分隔，体现面向分布式*/
        String connectStr = "47.100.49.95:2181,101.132.109.12:2181";
        /**连接超时时间*/
        int sessionTimeout = 2000;
        /**创建客户端连接对象 zooKeeper，客户端都是通过 客户端连接对象 操作服务端*/
        /**ZooKeeper类中包含 Watcher 类对象，当监听节点状态发生变化时，ZooKeeper里面的方法可以调用
         *  Watcher 类的 process()方法*/
        zooKeeper = new ZooKeeper(connectStr, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("path = " + watchedEvent.getPath() +
                        " type = " + watchedEvent.getType() + " state = " + watchedEvent.getState());
                try {
                    /**exists()方法会启动线程死循环检查GROUPNODE节点，当节点发生变化时，线程调用监听器
                     * 监听方法watcher.process()方法；*/
                    zooKeeper.exists(GROUPNODE,true);
                    /**对根节点GROUPNODE的所有子节点进行监听*/
                    /**getChildren()方法会启动线程死循环检查GROUPNODE节点的子节点，当子节点发生变化时
                     * ，线程调用监听器监听方法 watcher.process()方法；*/
                    zooKeeper.getChildren(GROUPNODE,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /**exists()方法会启动线程死循环检查GROUPNODE节点，当节点发生变化时，线程调用监听器
         * 监听方法watcher.process()方法；*/
        zooKeeper.exists(GROUPNODE,true);
        /**对根节点GROUPNODE的所有子节点进行监听*/
        /**getChildren()方法会启动线程死循环检查GROUPNODE节点的子节点，当子节点发生变化时
         * ，线程调用监听器监听方法 watcher.process()方法；*/
        zooKeeper.getChildren(GROUPNODE,true);
        Thread.sleep(Long.MAX_VALUE);
        /**释放连接*/
        zooKeeper.close();
    }
}
