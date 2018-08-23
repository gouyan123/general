package com.gupaoedu.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOClient {
	private final InetSocketAddress serverAdrress = new InetSocketAddress("localhost", 8080);
    private Selector selector = null;
    private SocketChannel clientSocketChannel = null;
    private String nickName = "";
    private Charset charset = Charset.forName("UTF-8");
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    private static String USER_CONTENT_SPILIT = "#@#";

    public NIOClient() throws IOException{
        /**创建 客户端连接通道*/
        clientSocketChannel = SocketChannel.open(serverAdrress);
        clientSocketChannel.configureBlocking(false);
        /**获取而不是创建 selector对象*/
        selector = Selector.open();
        /**将客户端连接通道及其key 注册到selector上，key表示该通道可向缓冲区读取数据*/
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
    }
    
    public void session(){
    	//开辟一个新线程从服务器端读数据
        new Reader().start();
        //开辟一个新线程往服务器端写数据
        new Writer().start();
	}
    
    private class Writer extends Thread{
		@Override
		public void run() {
			try{
				//在主线程中 从键盘读取数据输入到服务器端
		        Scanner scan = new Scanner(System.in);
		        while(scan.hasNextLine()){
		            String line = scan.nextLine();
		            if("".equals(line)) continue; //不允许发空消息
		            if("".equals(nickName)) {
		            	nickName = line;
		                line = nickName + USER_CONTENT_SPILIT;
		            } else {
		                line = nickName + USER_CONTENT_SPILIT + line;
		            }
                    /**将键盘输入内容写入 客户端连接通道*/
                    clientSocketChannel.write(charset.encode(line));
		        }
		        scan.close();
			}catch(Exception e){
			}
		}
    }

    private class Reader extends Thread {
        public void run() {
            try {
            	//轮询
                while(true) {
                    /**注册到select上的通道的数量*/
                    int readyChannels = selector.select();
                    if(readyChannels == 0) continue;
                    /**获取注册到selector上面的所有 key*/
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                    while(keyIterator.hasNext()) {
                         SelectionKey key = (SelectionKey) keyIterator.next();
                         keyIterator.remove();
                         process(key);
                    }
                }
            }
            catch (IOException e){
            	e.printStackTrace();
            }
        }

        private void process(SelectionKey key) throws IOException {
            /**注册到selector上面的客户端连接通道channel 可读，则取出封装在key中的通道，读到缓冲区*/
            if(key.isReadable()){
                /**取出封装到 key中的 channel*/
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer buff = ByteBuffer.allocate(1024);
                String content = "";
                while(sc.read(buff) > 0){
                    /**flip()切换 缓冲区读写模式*/
                    buff.flip();
                    content += charset.decode(buff);
                }
                //若系统发送通知名字已经存在，则需要换个昵称
                if(USER_EXIST.equals(content)) {
                	nickName = "";
                }
                System.out.println(content);
                /**取消 读 事件*/
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    public static void main(String[] args) throws IOException{
        new NIOClient().session();
    }
}
