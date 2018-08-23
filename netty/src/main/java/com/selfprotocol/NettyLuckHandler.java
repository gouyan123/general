package com.selfprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class NettyLuckHandler extends SimpleChannelInboundHandler<LuckMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LuckMessage msg) throws Exception {
        // 简单地打印出server接收到的消息
        System.out.println("客户端连接成功");
        System.out.println("msg : " + msg.toString());
        int version = 2;
        String sessionId = UUID.randomUUID().toString();
        String content = "You are OK!";

        LuckHeader header = new LuckHeader(version, content.length(), sessionId);
        LuckMessage luckMessage = new LuckMessage(header, content);
        ctx.writeAndFlush(luckMessage);
    }
}