package com.selfprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LuckClientHandler extends SimpleChannelInboundHandler<LuckMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LuckMessage msg) throws Exception {
        System.out.println("进入客户端");
        System.out.println("client msg : " + msg.toString());
    }
}