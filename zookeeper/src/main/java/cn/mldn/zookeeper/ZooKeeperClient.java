package cn.mldn.zookeeper;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.util.List;

public class ZooKeeperClient {
    /**根节点路径*/
    private static final String GROUPNODE = "/mldndata";
    /**子节点路径*/
    private static final String SUBNODE = GROUPNODE + "/bigdata";
    public static void main(String[] args) throws Exception {

        /**连接地址，多个连接地址使用 , 分隔，体现面向分布式*/
        String connectStr = "47.100.49.95:2181,101.132.109.12:2181";
        /**连接超时时间*/
        int sessionTimeout = 2000;
        /**创建客户端连接对象 zooKeeper，客户端都是通过 客户端连接对象 操作服务端*/
        ZooKeeper zooKeeper = new ZooKeeper(connectStr, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("path = " + watchedEvent.getPath() +
                        " type = " + watchedEvent.getType() + " state = " + watchedEvent.getState());
            }
        });
        /**创建父节点*/
        /*先判断节点是否存在，不存在，则创建*/
        if (zooKeeper.exists(GROUPNODE,false) == null){
            /**创建节点，各参数含义：节点路径；节点里面数据；安全认证模式；节点创建模式；*/
            /**此处创建的是 CreateMode.PERSISTENT 持久节点，即 客户端断开连接后，服务端保存该节点*/
            String groundNodePath = zooKeeper.create(GROUPNODE,"HelleData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("groundNodePath = " + groundNodePath);
        }
        /**创建子节点*/
        /*先判断节点是否存在，不存在，则创建*/
        for (int i=0;i<10;i++) {
            if (zooKeeper.exists(SUBNODE, false) == null) {
                /**创建节点，各参数含义：节点路径；节点里面数据；安全认证模式；节点创建模式；*/
                /**CreateMode.EPHEMERAL_SEQUENTIAL 瞬时序列化节点，即 客户端断开连接后，服务端不保存该节点*/
                String nodePath = zooKeeper.create(SUBNODE, "HelleSub".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                System.out.println("nodePath = " + nodePath);
            }
        }
        /**获取根节点 / 下面的所有子节点*/
        List<String> children = zooKeeper.getChildren("/",false);
        for (String child : children){
            System.out.println("child = " + child);
        }
        /**取得节点数据*/
        /**存在该节点，就取该节点里面数据*/
        if (zooKeeper.exists(GROUPNODE,false) != null){
            /*path：节点路径；watch：是否进行监听；stat：取得相关统计信息*/
            byte[] bytes = zooKeeper.getData(GROUPNODE,false,new Stat());
            String data = new String(bytes);
            System.out.println("data = " + data);
        }
        /**修改节点数据*/
        /*存在该节点，就改该节点里面数据*/
        if (zooKeeper.exists(GROUPNODE,false) != null){
            /**path：节点路径；watch：是否进行监听；version：修改哪个版本的数据 -1代表当前版本；stat：版本信息；*/
            Stat stat = zooKeeper.setData(GROUPNODE,"HelloGROUP".getBytes(),-1);
            System.out.println("stat = " + JSON.toJSONString(stat));
        }
        /**避免 释放连接，释放客户端连接对象后，该客户端在服务器端创建的 瞬时节点将消失*/
        Thread.sleep(Long.MAX_VALUE);
        /**释放客户端连接对象*/
        zooKeeper.close();
    }
}
