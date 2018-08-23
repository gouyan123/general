package com.gupaoedu.rpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import java.util.concurrent.ConcurrentHashMap;

public class RpcRegistry {

    private int port;

    public RpcRegistry(int port) {
        this.port = port;
    }
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {/*配置具体的数据处理方式*/
                        @Override              /*每个客户端与服务端连接成功都将产生一个socketchannel*/
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**/
                            pipeline.addLast(new LengthFieldPrepender(4));
                            /*服务器接收到的内容要解码，使用netty提供的 ObjectDecoder 类*/
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            /*服务器发送出去的内容要进行编码，使用netty提供的 ObjectEncoder 类*/
                            pipeline.addLast("encoder", new ObjectEncoder());
                            /*定义自己的处理逻辑*/
                            pipeline.addLast(new RegistryHandler());
                        }
                    });
            ChannelFuture f = b.bind(this.port).sync();
            System.out.println("服务已启动,监听端口" + this.port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        new RpcRegistry(8080).start();
    }
}
