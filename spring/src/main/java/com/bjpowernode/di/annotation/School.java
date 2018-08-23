package com.bjpowernode.di.annotation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "school")   /*①控制反转：容器利用无参构造，创建对象*/
public class School {
    @Value("清华")
    private String name;
    public School() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "School{" +
                "name='" + name + '\'' +
                '}';
    }
}
