package com.selfprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class LuckDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 获取协议的版本
        int version = in.readInt();
        // 获取消息长度
        int contentLength = in.readInt();
        // 获取SessionId
        byte[] sessionByte = new byte[36];
        in.readBytes(sessionByte);
        String sessionId = new String(sessionByte);

        // 组装协议头
        LuckHeader header = new LuckHeader(version, contentLength, sessionId);

        // 读取消息内容
        int length = in.readableBytes();
        byte[] content = new byte[length];
        //byte[] content = in.readBytes(in.readableBytes()).array();
        in.readBytes(content);
        LuckMessage message = new LuckMessage(header, new String(content));

        out.add(message);
    }
}