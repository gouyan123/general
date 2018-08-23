package com.rxl.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**两个独立进程相互通信*/
public class Server {
    public static void main(String[] args) {
        try {
            /*当前进程所在主机 IP 相当于一个房子，port 相当于一个门，这里主机ip(房子)有了，只需要一个port(门)*/
            ServerSocket serverSocket = new ServerSocket(10000);
            System.out.println("准备接收客户端...");
            boolean accept = true;
            /**注意，这里不能直接写 true ，因为这里写true，意味serverSocket.close永远不会执行*/
            while (accept){
                /**接收客户端socket*/
                /**进程会停在这里，直到有客户端来访问，才会继续往下走*/
                Socket socket = serverSocket.accept();
                System.out.println("接收到客户端socket = " + socket);/*进程会阻塞在上一句，不执行这句，直到客户端发送来socket*/
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
