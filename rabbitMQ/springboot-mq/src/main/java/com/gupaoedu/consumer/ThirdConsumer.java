package com.gupaoedu.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component

public class ThirdConsumer {

    @RabbitListener(queues = "THIRD_QUEUE")
    public void process(String msg){
        System.out.println(" third queue received msg : " + msg);
    }
}
