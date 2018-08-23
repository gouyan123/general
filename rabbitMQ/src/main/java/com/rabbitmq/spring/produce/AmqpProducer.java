package com.rabbitmq.spring.produce;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmqpProducer {

    private AmqpTemplate amqpTemplate;

    @Autowired
    public void amqpTemplate(ConnectionFactory connectionFactory) {
        amqpTemplate = new RabbitTemplate(connectionFactory);
    }

    /**将消息发送到指定的交换器上*/
    public void publishMsg(String exchange, String routingKey, Object msg) {
        amqpTemplate.convertAndSend(exchange, routingKey, msg);
    }
}