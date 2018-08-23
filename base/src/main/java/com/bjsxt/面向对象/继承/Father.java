package com.bjsxt.面向对象.继承;

public class Father {
    private String name;
    private int age;

    public Father() {
        String className = this.getClass().getName();
        System.out.println(className);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }


    public static void main(String[] args) {
        /*Son son = new Son();*/
        GrandSon grandSon = new GrandSon();
    }
}

class Son extends Father{
    public Son() {
        String className = this.getClass().getName();
        System.out.println(className);
    }
}

class GrandSon extends Son{
    public GrandSon() {
        String className = this.getClass().getName();
        System.out.println(className);
    }
}
