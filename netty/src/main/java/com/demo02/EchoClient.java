package com.demo02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/*netty创建客户端*/
public class EchoClient {
    private final String host;
    private final int port;
    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoClient("127.0.0.1",8080).start();
    }
    public void start() throws InterruptedException {
        final EchoClientHandler echoClientHandler = new EchoClientHandler();
        /*创建NioEventLoopGroup实例进行事件管理，例如，创建客户端新的连接和读写操作*/
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            /*创建 Bootstrap 实例，来初始化客户端*/
            Bootstrap b = new Bootstrap();
            b.group(group);
            /*创建客户端连接通道 NioSocketChannel，与服务端不同，服务端时 NioServerSocket*/
            b.channel(NioSocketChannel.class);
            b.remoteAddress(new InetSocketAddress(this.host, this.port));
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(echoClientHandler);
                }
            });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
