package com.demo02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/*标示一个ChannelHandler 可以被多个客户端Channel 安全地共享，该ChannelHanlder处理客户端Channel*/
/*ChannelHandler 该组件实现了服务器对从客户端接收的数据的处理*/
/*ChannelHandler，它是一个接口族的父接口，它的实现负责接收并响应事件通知*/
/*因为你的Echo 服务器会响应传入的消息，所以它需要实现ChannelInboundHandler 接口，用来定义响应入站事
* 件的方法。这个简单的应用程序只需要用到少量的这些方法，所以继承ChannelInboundHandlerAdapter 类也就足
* 够了，它提供了ChannelInboundHandler的默认实现*/
/****************************************************************************************/
/*ChannelInboundHandlerAdapter 有一个直观的API，并且它的每个方法都可以被重写以挂钩到事件生命周期的恰
当点上；*/
/****************************************************************************************/
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /*channelRead()方法：当客户端有消息传入时，会被事件状态挂钩调用，把客户端channel中的数据读进来
    * 并进行处理*/

    /*EchoServerHandler类与EchoClientHandler类中的方法执行顺序*/
    /*②*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        /*将消息输出到控制台*/
        System.out.println("Server received : " + in.toString(CharsetUtil.UTF_8));
        /*将接收到的消息写给客户端发送者，而不冲刷出站消息*/
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*将未决消息冲刷到远程节点，并且关闭该Channel*/
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
