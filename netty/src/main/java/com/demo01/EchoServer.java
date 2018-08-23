package com.demo01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup eventLoopGroup = null;
        try {
            //server端引导类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /*NioEventLoopGroup实际就是Reactor线程池，是工作线程NioEventLoop的数组，负责调度和执行客户端
            * 接入，网络读写事件处理，用户自定义任务和定时任务的执行*/
            eventLoopGroup = new NioEventLoopGroup();
            serverBootstrap.group(eventLoopGroup)
                    /*创建服务端 NioServerSocketChannel 用于监听端口和客户端连接*/
                    .channel(NioServerSocketChannel.class)

                    .localAddress("localhost",port)//设置InetSocketAddress让服务器监听某个端口已等待客户端连接。
                    .childHandler(new ChannelInitializer<Channel>() {//设置childHandler执行所有的连接请求
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            // 注册两个InboundHandler，执行顺序为注册顺序，所以应该是InboundHandler1 InboundHandler2
                            // 注册两个OutboundHandler，执行顺序为注册顺序的逆序，所以应该是OutboundHandler2 OutboundHandler1
                            ch.pipeline().addLast(new EchoInHandler1());
                            ch.pipeline().addLast(new EchoOutHandler1());
                            ch.pipeline().addLast(new EchoOutHandler2());
                            ch.pipeline().addLast(new EchoInHandler2());

                        }
                    });
            // 最后绑定服务器等待直到绑定完成，调用sync()方法会阻塞直到服务器完成绑定,然后服务器等待通道关闭，因为使用sync()，所以关闭操作也会被阻塞。
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("开始监听，端口为：" + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(20000).start();
    }
}
