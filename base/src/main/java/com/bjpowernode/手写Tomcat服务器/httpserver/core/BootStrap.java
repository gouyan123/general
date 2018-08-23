package com.bjpowernode.手写Tomcat服务器.httpserver.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BootStrap {
    /**主程序*/
    public static void main(String[] args) {
        start();
    }
    /**主程序入口*/
    public static void start() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader br = null;
        try {
            //获取当前时间
            long start = System.currentTimeMillis();
            //获取系统端口号
            int port = ServerParser.getPort();
            //解析服务中包含的web.xml配置文件
            String[] webAppNames = {"oa"};
            WebParser.parser(webAppNames);
            //创建服务器套接字，并且绑定端口号:8080
            serverSocket = new ServerSocket(port);
            //获取结束时间
            long end = System.currentTimeMillis();
            System.out.println("服务器已启动：" + (end - start));
            while(true){
                //开始监听网络，此时程序处于等待状态，等待接收客户端的消息
                clientSocket = serverSocket.accept();
				/*//接收客户端消息
				br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String temp = null;
				while((temp = br.readLine()) != null){
					System.out.println(temp);
				}*/
				/**以上操作交给HandlerRequest类的run()方法处理*/
               new Thread(new HandlerRequest(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
