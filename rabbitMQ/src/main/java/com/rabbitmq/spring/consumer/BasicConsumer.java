package com.rabbitmq.spring.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

public class BasicConsumer implements ChannelAwareMessageListener {
    private String name;

    public BasicConsumer(String name) {
        this.name = name;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            byte[] bytes = message.getBody();
            String data = new String(bytes, "utf-8");
            System.out.println(name + " data: " + data + " tagId: " + message.getMessageProperties().getDeliveryTag());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}