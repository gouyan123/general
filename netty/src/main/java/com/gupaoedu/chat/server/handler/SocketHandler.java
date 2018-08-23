package com.gupaoedu.chat.server.handler;

import com.gupaoedu.chat.protocol.IMMessage;
import org.apache.log4j.Logger;

import com.gupaoedu.chat.processor.MsgProcessor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

                                                              /**将客户端发送内容编解码为IMMessage对象*/
public class SocketHandler extends SimpleChannelInboundHandler<IMMessage>{
	private static Logger LOG = Logger.getLogger(SocketHandler.class);
	private MsgProcessor processor = new MsgProcessor();

	/**当客户端连接通道 channel可读时(通道中有内容时通道可读)，调用该方法*/
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {
		processor.sendMsg(ctx.channel(), msg);
	}
    /**①当客户端连接通道 channel增加时，调用该方法*/
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOG.info("服务端Handler创建...");
        super.handlerAdded(ctx);
    }
    /**当客户端连接通道 channel减少时，调用该方法*/
    @Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel client = ctx.channel();
		processor.logout(client);
		LOG.info("Socket Client:" + processor.getNickName(client) + "离开");
	}
    /**当客户端连接通道 channel连接时，调用该方法*/
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        LOG.info("客户端通道连接成功");
        System.out.println("客户端通道连接成功");
    }
    /**②tcp链路建立成功后调用*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("Socket Client: 有客户端连接："+ processor.getAddress(ctx.channel()));
    }
    /**异常处理*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.info("Socket Client: 与客户端断开连接:"+cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
