package com.factory.abstractFactory;

public class Student extends People {
    public static void main(String[] args) {
        new Student().drink();
    }

    @Override
    void drink() {
        System.out.println("喝墨水");
    }
}
