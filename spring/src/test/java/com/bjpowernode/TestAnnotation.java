package com.bjpowernode;

import com.bjpowernode.di.annotation.School;
import com.bjpowernode.di.annotation.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/*指定运行环境*/
@RunWith(SpringJUnit4ClassRunner.class)
/*读取配置文件，创建容器，相当于new ClassPathXmlApplicationContext("applicationContext1.xml");*/
@ContextConfiguration(value = "classpath:applicationContext1.xml")
public class TestAnnotation {
    /*@Test
    public void test01(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext1.xml");

        School school = (School) ac.getBean("school");
        System.out.println(school);
        Student student = (Student) ac.getBean("student");
        System.out.println(student);
    }*/
    @Autowired  /*从容器中注入school对象，相当于ac.getBean("school")*/
    private School school;
    @Test
    public void test02(){
        System.out.println(this.school);
    }
}
