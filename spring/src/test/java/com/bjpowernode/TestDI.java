package com.bjpowernode;

import com.bjpowernode.di.Person;
import com.bjpowernode.di.Student;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDI {

    @Test
    public void test01(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student = (Student) ac.getBean("student");
        System.out.println(student);
        Person person = (Person) ac.getBean("person");
        System.out.println(person);
    }
}
