package com.bjpowernode.di.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "student")/*1、控制反转*/
public class Student {

    @Value(value = "gouyan")    /*2、依赖注入*/
    public String name;
    @Value(value = "29")    /*2、依赖注入*/
    public int age;
    @Autowired    /*2、依赖注入*/
    @Qualifier(value = "school")/*两个注解联合使用*/
    private School school;

    public Student() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", school=" + school +
                '}';
    }
}
