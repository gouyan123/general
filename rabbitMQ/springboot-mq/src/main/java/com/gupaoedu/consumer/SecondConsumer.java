package com.gupaoedu.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SecondConsumer {

    @RabbitListener(queues = "SECOND_QUEUE",autoStartup = "false")
    public void process(String msg){
        System.out.println(" second queue received msg : " + msg);
    }
}
