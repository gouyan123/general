package com.dongnaoedu.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerConfirm {
    private final static String EXCHANGE_NAME = "producer_confirm";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**创建工厂*/
        ConnectionFactory factory = new ConnectionFactory();
        /**配置工厂*/
        factory.setHost("172.17.1.247");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        /**工厂创建 tcp/ip 连接*/
        Connection connection = factory.newConnection();
        /**在tcp/ip连接上面创建 虚拟信道，使用虚拟信道进行操作，节约tcp/ip连接资源；
         * 虚拟信道可以无限创建；*/
        Channel channel = connection.createChannel();
        /**将信道设置为 发送方确认模式*/
        channel.confirmSelect();
        /**信道中增加监听器，当服务端接收到数据后，远程调用监听方法*/
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("发送端确认 服务端 已接收到消息");
            }
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("发送端确认 服务端 未接收到消息");
            }
        });
        /**生产者 声明 交换空间(name + type)*/
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        for(int i=0;i<10;i++){
            String message = "Hello world_"+(i+1);
            channel.basicPublish(EXCHANGE_NAME,"pc",null,message.getBytes());
            /**信道等待rabbitmq服务端发送接收确认，接收到后服务端返回true*/
            if (channel.waitForConfirms()){
                System.out.println("Sent " + ":"+message);
            }
        }
        channel.close();
        connection.close();
    }
}
