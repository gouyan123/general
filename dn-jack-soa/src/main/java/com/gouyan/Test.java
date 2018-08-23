package com.gouyan;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:gouyan.xml");
        User user = (User) context.getBean("gy");
        System.out.println("------------" + JSON.toJSONString(user));
    }
}
