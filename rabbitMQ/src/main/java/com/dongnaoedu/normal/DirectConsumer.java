package com.dongnaoedu.normal;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectConsumer {
    private static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] argv) throws IOException,
            InterruptedException, TimeoutException {
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
        /**消费者 声明 交换空间(name + type)*/
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        /**消费者 声明随机队列，声明队列后无法订阅队列*/
        String queueName = channel.queueDeclare().getQueue();
        String[]serverities = {"error","info","warning"};
        for(String server:serverities){
            /**消费者将 队列及其路由 绑定到交换空间*/
            channel.queueBind(queueName,EXCHANGE_NAME,server);
        }
        System.out.println("Waiting message.......");
        /**要监听，需要创建 consumer接口对象*/
        Consumer consumerA = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("Accept:"+envelope.getRoutingKey()+":"+message);
            }
        };
        /**消费者 消费队列；true表示消费者自动确认，可以发送下一条了；当消费者取得队列中消息时，自动
         * 调用 监听器consumerA的监听方法*/
        channel.basicConsume(queueName,true,consumerA);
    }
}
