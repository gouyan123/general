package com.demo;

public class Demo {
    public static void main(String[] args) {
        /*Person p1 = new Person();
        p1.setName("James");
        Person p2 = new Person();
        p2.setName("James");
        System.out.println(p1.equals(p2));*/
        String str1 = "abc";
        String str2 = "abc";
        System.out.println(str1 == str2);
        String str3 = new String("abc");
        String str4 = new String("abc");
        System.out.println(str3 == str4);
        System.out.println(str3.equals(str4));
    }
}
class Person{
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
