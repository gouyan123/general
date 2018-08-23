package com.rabbitmq.original;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MsgConsumer {
    public static void consumeMsg(String queueName,String exchangeName,String rountingKey) throws IOException, TimeoutException {
        ConnectionFactory factory = RabbitUtil.getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        /**连接对象channel在rabbitmq服务器端创建 对列queueName*/
        channel.queueDeclare(queueName,true,false,false,null);
        /**连接对象channel将创建队列queueName保定到交换空间exchangeName上*/
        channel.queueBind(queueName,exchangeName,rountingKey);
        /**创建消费者对象*/
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    System.out.println(queueName + " Received '" + message);
                } finally {
                    System.out.println(queueName + " Done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        /**自动取消 ack*/
        /**Consumer接收到了消息之后，必须返回一个ack的标志，表示消息是否成功消费，如果返回true，则表
         * 示消费成功了，然后这个消息就会从RabbitMQ的队列中删掉；如果返回false，且设置为重新入队，则
         * 这个消息可以被重新投递进来*/
        /**basickConsume()方法中启动线程，死循环检查队列queueName，检查到数据则取出，并调用consumer
         * 对象中的相应方法*/
        channel.basicConsume(queueName,false,consumer);
        /**关闭通道，关闭连接*/
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        String exchangeName = "direct.exchange";
        String[] routingKey = new String[]{"aaa", "bbb"};
        String[] queueNames = new String[]{"qa", "qb"};
        for (int i = 0; i < 2; i++) {
            consumeMsg(queueNames[i],exchangeName,routingKey[i]);
        }
        try {
            Thread.sleep(1000 * 60 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
