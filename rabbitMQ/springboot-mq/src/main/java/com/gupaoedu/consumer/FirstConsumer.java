package com.gupaoedu.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FirstConsumer {

    @RabbitListener(queues = "FIRST_QUEUE",autoStartup = "false")
    public void process(String msg){
        System.out.println(" first queue received msg : " + msg);
    }
}
