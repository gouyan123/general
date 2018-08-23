package com.configuration;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
//@ContextConfiguration(classes = ApplicationConfig.class)
public class TestConfiguration {
    @Autowired
    private Apple apple;
    @Test
    public void test01(){
        System.out.println(apple.getColor());
    }
}
