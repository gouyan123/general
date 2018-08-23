package com.rabbitmq.original;

import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitUtil {
    private static ConnectionFactory factory = new ConnectionFactory();
    public static ConnectionFactory getConnectionFactory() throws IOException, TimeoutException{
        factory.setHost("47.100.49.95");
        factory.setPort(5672);
        factory.setUsername("gouyan");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        return factory;
    }
}