package com.demo02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/*标记该类的实例可以被多个Channel共享*/
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    /*channelActive()方法：与服务器成功连接后，将被事件状态挂钩调用*/
    /*EchoServerHandler类与EchoClientHandler类中的方法执行顺序*/
    /*①*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端成功连接服务端");
        /*客户端向服务端发送消息，内容为 Netty "rocks!" */
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }
    /*channelRead0()方法：当接收到服务器发送消息时，被事件状态挂钩调用*/
    /*服务器发送的消息被分块接收，因此channelRead0()方法可能会被多次调用*/
    /*③*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("客户端接收到服务端发送到消息");
        System.out.println("Client received : " + msg.toString(CharsetUtil.UTF_8));
    }
    /*处理过程中发生异常，将被事件状态挂钩调用*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
