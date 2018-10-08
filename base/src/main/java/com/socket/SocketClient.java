package com.socket;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) {
        try {
            Socket s = new Socket();
//            s.setSoLinger(true,0);
            s.connect(new InetSocketAddress("127.0.0.1",3113));

            OutputStream os = s.getOutputStream();
            os.write("hello".getBytes());

            s.close();

            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
