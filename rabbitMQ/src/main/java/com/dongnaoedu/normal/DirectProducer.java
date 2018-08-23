package com.dongnaoedu.normal;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectProducer {
    private final static String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        /**创建工厂*/
        ConnectionFactory factory = new ConnectionFactory();
        /**配置工厂*/
        factory.setHost("47.100.49.95");
        factory.setPort(5672);
        factory.setUsername("gouyan");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        /**工厂创建 tcp/ip 连接*/
        Connection connection = factory.newConnection();
        /**在tcp/ip连接上面创建 虚拟信道，使用虚拟信道进行操作，节约tcp/ip连接资源；
         * 虚拟信道可以无限创建；*/
        Channel channel = connection.createChannel();
        /**生产者 声明 交换空间(name + type)*/
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String[]serverities = {"error","info","warning"};
        for(int i=0;i<3;i++){
            String server = serverities[i];
            String message = "Hello world_"+(i+1);
            /**生产者将 消息及其路由 通过信道 发布到 交换空间*/
            channel.basicPublish(EXCHANGE_NAME,server,null,message.getBytes());
            System.out.println("Sent(路由 + 消息体) "+server+":"+message);
        }
        channel.close();
        connection.close();
    }
}
