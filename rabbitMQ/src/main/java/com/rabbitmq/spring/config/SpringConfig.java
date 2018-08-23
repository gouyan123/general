package com.rabbitmq.spring.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.*;

@Configuration
@Import(value = {DirectConsumerConfig.class})
@ComponentScan(value="com.rabbitmq.produce")
public class SpringConfig {

    /*private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        String host = environment.getProperty("rabbitmq.host",String.class);
        int port = environment.getProperty("rabbitmq.port",int.class);
        System.out.println("host = " + host + " port " + port);
    }*/

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("47.100.49.95");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("gouyan");
        connectionFactory.setPassword("123456");

        connectionFactory.setChannelCacheSize(8);
        return connectionFactory;
    }
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}