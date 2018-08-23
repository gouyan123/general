package com.selfprotocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.UUID;

public class LuckClient {
    private static final LuckEncoder ENCODER = new LuckEncoder();
    public static void main(String args[]) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /*服务端增加解码协议*/
                            pipeline.addLast(new LuckDecoder());
                            pipeline.addLast(ENCODER);
                            pipeline.addLast(new LuckClientHandler());
                        }
                    });

            // Start the connection attempt.
            ChannelFuture f = b.connect("127.0.0.1", 8888).sync();
            Channel ch = f.channel();
            int version = 1;
            String sessionId = UUID.randomUUID().toString();
            String content = "I'm the luck protocol!";

            LuckHeader header = new LuckHeader(version, content.length(), sessionId);
            LuckMessage message = new LuckMessage(header, content);
            ch.writeAndFlush(message);

            //ch.close();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}