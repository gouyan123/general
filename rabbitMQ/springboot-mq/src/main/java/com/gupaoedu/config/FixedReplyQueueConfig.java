package com.gupaoedu.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class FixedReplyQueueConfig {

    @Bean
    public ConnectionFactory connectionFactory(){
        //com.rabbitmq.client.ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("${spring.rabbitmq.host}");
        connectionFactory.setPort(Integer.parseInt("${spring.rabbitmq.port}"));
        connectionFactory.setVirtualHost("${spring.rabbitmq.virtual-host}");
        connectionFactory.setUsername("${spring.rabbitmq.username}");
        connectionFactory.setPassword("${spring.rabbitmq.password}");
//        ExecutorService service= Executors.newFixedThreadPool(20);//500个线程的线程池
//        connectionFactory.setSharedExecutor(service);
        return connectionFactory;
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory());
        cachingConnectionFactory.setPublisherConfirms(true);    //开启发送确认
        return cachingConnectionFactory;
    }

    //定义rabbitmqTemplate
    @Bean(name = "fixedReplyQRabbitTemplate")
    public RabbitTemplate fixedReplyQRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory());
        template.setExchange(requestExchange().getName());
        template.setRoutingKey("PRE_RPC");
        template.setReplyAddress(requestExchange().getName() + "/" + replyQueue().getName());
        //template.setReceiveTimeout(60000);
        template.setReplyTimeout(60000);
        return template;
    }


    //rabbitmqTemplate监听返回队列的消息
    @Bean
    public SimpleMessageListenerContainer replyListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory());
        container.setQueues(replyQueue());
        container.setMessageListener(fixedReplyQRabbitTemplate());
        ExecutorService executorService = Executors.newFixedThreadPool(300);  //300个线程的线程池
        container.setTaskExecutor(executorService);
        container.setConcurrentConsumers(200);
        container.setPrefetchCount(5);
        return container;
    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer){
//        SimpleRabbitListenerContainerFactory factory=new SimpleRabbitListenerContainerFactory();
//        ExecutorService service=Executors.newFixedThreadPool(600);
//        factory.setTaskExecutor(service);
//        factory.setConcurrentConsumers(500);
//        factory.setPrefetchCount(5);
//        configurer.configure(factory,cachingConnectionFactory());
//        return factory;
//    }

    //请求队列和交换器绑定
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(requestQueue()).to(requestExchange()).with("PRE_RPC");
    }

    //返回队列和交换器绑定
    @Bean
    public Binding replyBinding() {
        return BindingBuilder.bind(replyQueue())
                .to(requestExchange())
                .with(replyQueue().getName());
    }

    //请求队列
    @Bean
    public Queue requestQueue() {
        String queueName = "mq.pre.queue";
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        return new Queue(queueName,durable,exclusive,autoDelete);
    }

    //每个应用实例监听的返回队列
    @Bean
    public Queue replyQueue() {
        String queueName = "mq.pre.queue" + UUID.randomUUID().toString();
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        return new Queue(queueName,durable,exclusive,autoDelete);
    }

    //交换器
    @Bean
    public DirectExchange requestExchange() {
        return new DirectExchange("PRE_DIRECT_EXCHANGE", false, true);
    }


    @Bean
    public RabbitAdmin admin() {
        return new RabbitAdmin(rabbitConnectionFactory());
    }
}
