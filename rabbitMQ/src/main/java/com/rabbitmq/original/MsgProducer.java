package com.rabbitmq.original;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MsgProducer {
    public static void produceMsg(String exchangeName, BuiltinExchangeType type,String rountingKey,String message) throws IOException, TimeoutException {
        ConnectionFactory factory = RabbitUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        /**连接对象channel创建交换空间，true消息可持久，false消息不自动删除*/
        channel.exchangeDeclare(exchangeName,type,true,false,null);
        /**连接对象channel发送消息，rountingKey为消息地址，data为消息内容*/
        channel.basicPublish(exchangeName,rountingKey,null,message.getBytes());
        channel.close();
        connection.close();

    }

    public static void main(String[] args) throws IOException, TimeoutException {
        String exchangeName = "direct.exchange";
        BuiltinExchangeType type = BuiltinExchangeType.DIRECT;
        /**向exchangeName发送消息(消息地址 + 消息内容)，发送方式为direct*/
        String[] routingKey = new String[]{"aaa", "bbb"};
        String msg = "hello >>> ";
        for (int i = 0; i < 30; i++) {
            produceMsg(exchangeName,type,routingKey[i % 2], msg + i);
        }
        System.out.println("----over-------");
    }
}