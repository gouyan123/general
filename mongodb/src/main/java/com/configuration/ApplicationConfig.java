package com.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
public class ApplicationConfig {
    @Bean(name = "apple")
    public Apple apple(){
        Apple apple = new Apple();
        apple.setColor("red");
        return apple;
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Apple apple = (Apple) context.getBean("apple");
        System.out.println(apple.getColor());
    }
}

