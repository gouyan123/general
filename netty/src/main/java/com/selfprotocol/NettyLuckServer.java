package com.selfprotocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyLuckServer {

    // 指定端口号
    private static final int PORT = 8888;
    private static final LuckEncoder ENCODER = new LuckEncoder();
    public static void main(String args[]) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 指定socket的一些属性
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  // 指定是一个NIO连接通道
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /*服务端增加解码协议*/
                            pipeline.addLast(ENCODER);
                            pipeline.addLast(new LuckDecoder());
                            pipeline.addLast(new NettyLuckHandler());
                        }
                    });

            // 绑定对应的端口号,并启动开始监听端口上的连接
            Channel ch = serverBootstrap.bind(PORT).sync().channel();

            System.out.printf("luck协议启动地址：127.0.0.1:%d/\n", PORT);

            // 等待关闭,同步端口
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}