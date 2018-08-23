package com.gupaoedu.chat.processor;

import com.alibaba.fastjson.JSONObject;
import com.gupaoedu.chat.protocol.IMDecoder;
import com.gupaoedu.chat.protocol.IMEncoder;
import com.gupaoedu.chat.protocol.IMMessage;
import com.gupaoedu.chat.protocol.IMP;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

public class IMProcessor {
    //记录在线用户，ChannelGroup为客户端连接通道Channel的集合
    private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //自定义解码器
    private IMDecoder decoder = new IMDecoder();
    //自定义编码器
    private IMEncoder encoder = new IMEncoder();
    //定义一些扩展属性
    private final AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
    private final AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
    private final AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");


    /*接收到客户端 channel，及请求信息 msg，将msg解码后，取出 信息类型(SYSTEM,LOGIN...)，根据信息
    * 类型进行相应操作，并将结果编码后，返回给chat.util.js的 CHAT.socket.onmessage = function(e) {...}
     * 方法，该方法接到 返回结果后，渲染到 chat.html中*/
    public void process(Channel client,String msg){
        IMMessage request = this.decoder.decode(msg);
        if (request == null){return;}
        String nickName = request.getSender();
        /*如果是登录操作，就向 ChannelGroup 中增加一个 channel*/
        if (request.getCmd().equals(IMP.LOGIN.getName())){
            client.attr(this.NICK_NAME).getAndSet(request.getSender());
            client.attr(IP_ADDR).getAndSet(request.getAddr());
            IMProcessor.onlineUsers.add(client);
            /*上线通知，通知其他用户该用户已上线*/
            for (Channel channel : IMProcessor.onlineUsers){
                if (channel != client){
                    /*创建自定义消息对象*/
                    request = new IMMessage(IMP.SYSTEM.getName(),this.systTime(),onlineUsers.size(),nickName + " 加入聊天室");
                }else {
                    request = new IMMessage(IMP.SYSTEM.getName(),this.systTime(),onlineUsers.size(),"已加入聊天室");
                }
                /*发送前，对自定义消息对象进行编码*/
                String text = this.encoder.encode(request);
                /*发送已编码信息*/
                /*客户端 chat.util.js的 CHAT.socket.onmessage会接收到该内容*/
                channel.writeAndFlush(new TextWebSocketFrame(text));
            }
            /*客户端发送来 退出消息*/
        }else if (request.getCmd().equals(IMP.LOGOUT.getName())){
            IMProcessor.onlineUsers.remove(client);
            /*客户端发送来 聊天消息*/
        }else if (request.getCmd().equals(IMP.CHAT.getName())){
            /*发送聊天信息*/
            for (Channel channel : IMProcessor.onlineUsers){
                if (channel != client){
                    /*设置用户昵称*/
                    request.setSender(client.attr(NICK_NAME).get());
                }else {
                    request.setSender("you");
                }
                /*发送前，对自定义消息对象进行编码*/
                String text = this.encoder.encode(request);
                /*发送已编码信息*/
                /*客户端 chat.util.js的 CHAT.socket.onmessage会接收到该内容*/
                channel.writeAndFlush(new TextWebSocketFrame(text));
            }
        }
    }
    /*客户端退出，channelGroup中移除该group*/
    public void logout(Channel client){
        IMProcessor.onlineUsers.remove(client);
    }


    public long systTime(){
        return System.currentTimeMillis();
    }
}
