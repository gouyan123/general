package cn.mldn.zookeeper.serverlist;

import org.apache.zookeeper.*;

public class ServerListener {
    /**连接地址，多个连接地址使用 , 分隔，体现面向分布式*/
    private static final String connectStr = "47.100.49.95:2181,101.132.109.12:2181";
    /**连接超时时间*/
    private static final int sessionTimeout = 2000;
    /**根节点路径*/
    private static final String GROUPNODE = "/mldn-servers";
    /**子节点路径，序列节点会在 /server- 后面加编号*/
    private static final String SUBNODE = GROUPNODE + "/server-";
    private ZooKeeper zooKeeper;
    /**接收服务器名称，连接到zookeeper服务端并创建 持久化根节点/mldn-servers及 瞬时序列化子节点
     * /mldn-servers/server-编号，*/
    public ServerListener(String serverName) throws Exception {
        /*连接服务器，并创建 根节点，及下面子节点*/
        this.connectZooKeeperServer(serverName);
        this.handle();
    }

    public  void connectZooKeeperServer(String serverName) throws Exception {
        /**创建客户端连接对象 zooKeeper，客户端都是通过 客户端连接对象 操作服务端*/
        /**ZooKeeper类中包含 Watcher 类对象，当监听节点状态发生变化时，ZooKeeper里面的方法可以调用
         *  Watcher 类的 process()方法*/
        this.zooKeeper = new ZooKeeper(connectStr, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
        /**不存在 持久化根节点 /mldn-servers 则创建*/
        if (this.zooKeeper.exists(GROUPNODE,false) == null){
            this.zooKeeper.create(GROUPNODE,"ServerList".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        /**不存在 瞬时序列化子节点 /mldn-servers/server-编号 则创建*/
        if (this.zooKeeper.exists(SUBNODE,false) == null){
            /**serverName为服务器名称，java程序获得 根节点/mldn-servers 下面的所有子节点后，获得
             * 该serverName 的列表*/
            this.zooKeeper.create(SUBNODE,serverName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }
    }
    public void handle() throws InterruptedException {
        System.out.println("ServerListener UP!!!");
        /**业务处理*/
        Thread.sleep(Long.MAX_VALUE);
        /**关闭连接*/
        this.zooKeeper.close();
    }
}
