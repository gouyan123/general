package com.gupaoedu.catalina.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class GPTomcat {
    public void start(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();/*创建boss线程*/
        EventLoopGroup workerGroup = new NioEventLoopGroup();/*创建worker线程*/
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();/*启动Netty服务引擎*/
            /*给Netty服务引擎分配两个线程，group()代表单线程，group(EventLoopGroup group)，
            group(EventLoopGroup parentGroup,EventLoopGroup childGroup)主从线程*/
            /*主线程channel()利用反射实现主线程处理类NioServerSocketChannel*/
            /*子线程childHandler(子线程处理类)，有客户端连接进来以后要做业务逻辑处理，只要有客户端连接上来，
            就会触发这样一个事件，new ChannelInitializer<SocketChannel>() {...}是一个接口匿名对象，作为
            childHandler()方法参数，会在childHandler()方法中当满足条件时被调用，称为回调*/
            /*option配置主线程永远不停止，childOption()配置子线程*/
            bootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            /*无锁化串行编程，体现在 addLast()上，按顺序，从后到前依次执行*/

                            //服务端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            //服务端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());
                            //最后处理自己的逻辑
                            socketChannel.pipeline().addLast(new GPTomcatHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).
                    childOption(ChannelOption.SO_KEEPALIVE, true);
            /*启动服务ServerBootstrap，bind()绑定端口，sync()等待*/
            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("HTTP服务已启动，监听端口:" + port);
            /*开始接收客户*/
            f.channel().closeFuture().sync();
        }finally {
            /*最后关闭线程*/
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new GPTomcat().start(8080);
        System.out.println("你好");
    }
}
