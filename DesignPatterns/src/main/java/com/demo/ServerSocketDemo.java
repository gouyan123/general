package com.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketDemo {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);

            Socket socket = serverSocket.accept();
            //接收客户端消息
			BufferedReader	br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String temp = null;
            while((temp = br.readLine()) != null){
                System.out.println(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
