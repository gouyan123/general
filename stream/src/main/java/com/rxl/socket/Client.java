package com.rxl.socket;

import java.io.IOException;
import java.net.Socket;

/**两个独立进程相互通信*/
public class Client {
    public static void main(String[] args) {
        try {
            /*往这个ip，port发送socket后，会触发服务器端的 accept()方法执行，解除阻塞*/
            Socket socket = new Socket("127.0.0.1",10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
