package cn.mldn.zookeeper.serverlist;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ServerListClientListener {
    /**连接地址，多个连接地址使用 , 分隔，体现面向分布式*/
    private static final String connectStr = "47.100.49.95:2181,101.132.109.12:2181";
    /**连接超时时间*/
    private static final int sessionTimeout = 2000;
    /**根节点路径*/
    private static final String GROUPNODE = "/mldn-servers";
    /**子节点路径，序列节点会在 /server- 后面加编号*/
    private static final String SUBNODE = GROUPNODE + "/server-";
    private ZooKeeper zooKeeper;

    public ServerListClientListener() throws Exception {
        /*连接zookeeper服务端，获得 zookeeper客户端连接对象，可以操作zookeeper服务端*/
        this.connectZooKeeperServer();
        Set<String> servers = this.getServerList();
        System.out.println(JSON.toJSONString(servers,true));
        Thread.sleep(Long.MAX_VALUE);
    }

    private void connectZooKeeperServer() throws Exception {
        /**创建客户端连接对象，通过客户端连接对象操作服务端*/
        this.zooKeeper = new ZooKeeper(connectStr, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getPath() != null){
                    /*子节点发生变化*/
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                        try {
                            Set<String> servers = ServerListClientListener.this.getServerList();
                            System.out.println(JSON.toJSONString(servers));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        /*如果没有根节点 /mldn-servers 则创建*/
        if (this.zooKeeper.exists(GROUPNODE,false) == null){
            this.zooKeeper.create(GROUPNODE,"ServerList".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    public Set<String> getServerList() throws Exception {
        Set<String> servers = new LinkedHashSet<String>();
        /**获取根节点 GROUPNODE 节点下面的所有子节点；true监听：开启多线程死循环检查跟节点下面的子节点，当
         * 子节点变化时，线程调用监听器监听方法 watcher.process(event)*/
        List<String> children = this.zooKeeper.getChildren(GROUPNODE,true);
        /**child为 server-00x，即子节点相对路径*/
        for (String child : children){
            /*子节点全路径*/
            String path = GROUPNODE + "/" + child;
            /**获取子节点里面的数据*/
            byte[] bytes = this.zooKeeper.getData(path,false,new Stat());
            String serverName = new String(bytes);
            servers.add(serverName);
        }
        return servers;
    }

    public static void main(String[] args) {
        try {
            new ServerListClientListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
