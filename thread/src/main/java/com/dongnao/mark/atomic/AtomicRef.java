package com.dongnao.mark.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicRef {

    static class User{
        private String name;
        private Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }
    }
    static AtomicReference<User> ar = new AtomicReference<User>();
    public static void main(String[] args) {
        User old = new User("mark",30);
        ar.set(old);//原子操作获取user对象引用
        User new1 = new User("James",34);
        ar.compareAndSet(old,new1);
        System.out.println(ar.get());//获取user对象
        System.out.println(ar.get().getName());//获取user对象的name属性
        System.out.println(ar.get().getAge());//获取user对象的age属性
    }
}
