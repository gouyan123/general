package cn.mldn.main;

import cn.mldn.zookeeper.serverlist.ServerListener;

public class StartServerMain {
    /**通过args传服务器名 serverName*/
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("Please enter ServerName");
            /**退出系统*/
            System.exit(1);
        }
        try {
            /**启动 ServerListener 连接zookeeper，创建节点*/
            new ServerListener(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
