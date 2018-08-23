package com.gupaoedu.nio.server;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 网络多客户端聊天室
 * 功能1： 客户端通过Java NIO连接到服务端，支持多客户端的连接
 * 功能2：客户端初次连接时，服务端提示输入昵称，如果昵称已经有人使用，提示重新输入，如果昵称唯一，则登录成功，之后发送消息都需要按照规定格式带着昵称发送消息
 * 功能3：客户端登录后，发送已经设置好的欢迎信息和在线人数给客户端，并且通知其他客户端该客户端上线
 * 功能4：服务器收到已登录客户端输入内容，转发至其他登录客户端。
 * 
 * TODO 客户端下线检测
 */
public class NIOServer {

    private int port = 8080;
    private Charset charset = Charset.forName("UTF-8");
    //用来记录在线人数，以及昵称
    private static HashSet<String> users = new HashSet<String>();
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    //相当于自定义协议格式，与客户端协商好
    private static String USER_CONTENT_SPILIT = "#@#";
    private Selector selector = null;
    
    public NIOServer(int port) throws IOException{
		this.port = port;
		/**创建服务端连接通道channel对象 */
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		/**设置通道channel监听端口*/
        serverSocketChannel.bind(new InetSocketAddress(this.port));
        /**设置通道channel非阻塞*/
        serverSocketChannel.configureBlocking(false);
		/**获取而不是创建 selector对象*/
		selector = Selector.open();
		/**将服务端连接通道 serverSocketChannel封装到key中再注册到 selector上，key表示该通道可以
         * 被客户端连接通道连接了*/
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("服务已启动，监听端口是：" + this.port);
	}
    
    
    public void listener() throws IOException{
    	while(true) {
    		/**返回注册到 selector上面的 连接通道数量*/
            int waitNum = selector.select();
            if(waitNum == 0) {
                continue;
            }
            /**返回注册到 selector上面的所有客户端连接通道的 key*/
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				/*过号不候*/
				iterator.remove();
				/*处理逻辑*/
				process(key);
            }
        }
		
	}
    
    
    public void process(SelectionKey key) throws IOException {
    	/**表示服务端连接通道已准备好，可以被客户端连接*/
        if(key.isAcceptable()){
        	ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel clientSocketChannel = server.accept();
            /**客户端连接通道设置为非阻塞模式，即当缓冲区数据没有准备好，不等待*/
            clientSocketChannel.configureBlocking(false);
            /**将客户端连接通道及其 key注册到 selector上，key表示该通道可读*/
            clientSocketChannel.register(selector, SelectionKey.OP_READ);
            
            //将此对应的channel设置为准备接受其他客户端请求
            key.interestOps(SelectionKey.OP_ACCEPT);
            clientSocketChannel.write(charset.encode("请输入你的昵称"));
        }
        /**表示客户端连接通道 可以向缓冲区读数据*/
        if(key.isReadable()){
            /**取出 key中封装的 客户端连接通道对象*/
            SocketChannel clientSocketChannel = (SocketChannel)key.channel();
            /**客户端连接通道往缓冲区读数据*/
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try{
                while(clientSocketChannel.read(buff) > 0){
                    /**flip()转换缓冲区读写模式*/
                    buff.flip();
                    content.append(charset.decode(buff));
                }
                //将此对应的channel设置为准备下一次接受数据
                key.interestOps(SelectionKey.OP_READ);
            }catch (IOException io){
            	key.cancel();
                if(key.channel() != null){
                	key.channel().close();
                }
            }
            if(content.length() > 0) {
                String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
                //注册用户
                if(arrayContent != null && arrayContent.length == 1) {
                    String nickName = arrayContent[0];
                    if(users.contains(nickName)) {
                    	clientSocketChannel.write(charset.encode(USER_EXIST));
                    } else {
                        users.add(nickName);
                        int onlineCount = onlineCount();
                        String message = "欢迎 " + nickName + " 进入聊天室! 当前在线人数:" + onlineCount;
                        broadCast(null, message);
                    }
                } 
                //注册完了，发送消息
                else if(arrayContent != null && arrayContent.length > 1) {
                    String nickName = arrayContent[0];
                    String message = content.substring(nickName.length() + USER_CONTENT_SPILIT.length());
                    message = nickName + " 说 " + message;
                    if(users.contains(nickName)) {
                        //不回发给发送此内容的客户端
                    	broadCast(clientSocketChannel, message);
                    }
                }
            }
            
        }
    }

    public int onlineCount() {
        int res = 0;
        for(SelectionKey key : selector.keys()){
            Channel target = key.channel();
            
            if(target instanceof SocketChannel){
                res++;
            }
        }
        return res;
    }
    
    public void broadCast(SocketChannel clientSocketChannel, String content) throws IOException {
        /**循环注册到 selector上面的所有 key(key中封装通道channel)*/
        for(SelectionKey key : selector.keys()) {
            Channel targetchannel = key.channel();
            //如果client不为空，不回发给发送此内容的客户端
            if(targetchannel instanceof SocketChannel && targetchannel != clientSocketChannel) {
                SocketChannel target = (SocketChannel)targetchannel;
                target.write(charset.encode(content));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new NIOServer(8080).listener();
    }
}
