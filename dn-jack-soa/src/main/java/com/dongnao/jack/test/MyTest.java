package com.dongnao.jack.test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("mytest.xml");
       /* UserService userservice = context.getBean(UserService.class);
        String result = userservice.eat("xxxxx");
        System.out.println(result);*/
//        Jedis jedis = new Jedis("101.132.109.12",6379);
//        //jedis.auth(String.valueOf(123456));
//        System.out.println(jedis);
//        jedis.set("gy", String.valueOf(123));
//        String result = jedis.get("gy");
//        System.out.println("result = " + result);
    }
}
