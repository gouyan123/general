package com.demo02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

/*利用netty创建服务端*/
public class EchoServer {
    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(8080).start();
    }
    public void start() throws Exception {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        /*创建NioEventLoopGroup实例进行事件处理，例如，接受客户端新的连接SocketChannel和读写操作*/
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            /*创建 ServerBootstrap 实例，来初始化服务端*/
            ServerBootstrap b = new ServerBootstrap();
            b.group(group);
            /*创建服务端连接通道 NioServerSocketChannel*/
            b.channel(NioServerSocketChannel.class);
            b.localAddress(new InetSocketAddress(this.port));
            /*ChannelInitializer，这是关键，当一个新的连接被接受时，一个新的客户端通道Channel将会
            * 被创建，而ChannelInitializer 将会把你的EchoServerHandler的实例添加到该Channel的
            * ChannelPipeline 中。正如我们之前所解释的，这个ChannelHandler 将会收到有关入站消息的
            * 通知*/
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override                 /*客户端连接通道 Channel 被创建*/
                protected void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline pipeline = ch.pipeline();
                    /*使用EchoServerHandler的实例来初始化每一个新的客户端Channel*/
                    /*添加一个EchoServerHandler 到子Channel的ChannelPipeline*/
                    /*EchoServerHandler 被标注为@Shareable，所以我们可以总是使用同样的实例*/
                    /*这里对于所有的客户端连接来说，都会使用同一个EchoServerHandler，因为其被标注为@Sharable，
                    * 这将在后面的章节中讲到*/
                    pipeline.addLast(echoServerHandler);
                }
            });
            /*异步地绑定服务器端口*/
            /* xx().sync()：表示线程阻塞，直到前面到 xx()方法完成*/
            /* bind().sync()：sync()表示阻塞，直到前面到 bind()方法绑定完成*/
            ChannelFuture f = b.bind().sync();
            System.out.println("服务端已启动");
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
