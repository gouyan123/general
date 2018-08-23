package com.gupaoedu.chat.server.handler;

import com.gupaoedu.chat.processor.IMProcessor;
import org.apache.log4j.Logger;

import com.gupaoedu.chat.processor.MsgProcessor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

																/**表示将接收到的协议编解码为该类对象*/
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private static Logger LOG = Logger.getLogger(WebSocketHandler.class);
	private MsgProcessor processor = new MsgProcessor();
    private IMProcessor imProcessor = new IMProcessor();
	/**当客户端 chat.util.js 发送websocket协议请求时及CHAT.socket.send(msg)时，调用该方法*/
	@Override											  /**请发送来的内容解析为 TextWebSocketFrame*/
	protected void channelRead0(ChannelHandlerContext ctx,TextWebSocketFrame msg) throws Exception {
		/**接收到客户端 channel，及请求信息 msg，将msg解码后，取出 信息类型(SYSTEM,LOGIN...)，根据信息
		 * 类型进行相应操作*/
//	    this.imProcessor.process(ctx.channel(),msg.text());
		processor.sendMsg(ctx.channel(), msg.text());
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception { // (2)
		Channel client = ctx.channel();
		String addr = processor.getAddress(client);
		LOG.info("WebSocket Client:" + addr + "加入");
	}
    /*如果客户端退出，调用该监听方法*/
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel client = ctx.channel();
		processor.logout(client);
		LOG.info("WebSocket Client:" + processor.getNickName(client) + "离开");
		/*Channel client = ctx.channel();
		this.imProcessor.logout(client);*/
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
		Channel client = ctx.channel();
		String addr = processor.getAddress(client);
		LOG.info("WebSocket Client:" + addr + "上线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
		Channel client = ctx.channel();
		String addr = processor.getAddress(client);
		LOG.info("WebSocket Client:" + addr + "掉线");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		Channel client = ctx.channel();
		String addr = processor.getAddress(client);
		LOG.info("WebSocket Client:" + addr + "异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
