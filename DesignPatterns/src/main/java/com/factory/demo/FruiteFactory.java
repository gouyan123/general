package com.factory.demo;

public class FruiteFactory extends Factory {
    @Override
    public Fruite getApple() {
        return new Apple();
    }

    @Override
    public Fruite getOrange() {
        return new Orange();
    }

    @Override
    public Fruite getBanana() {
        return new Banana();
    }

    public static void main(String[] args) {
        Factory fruiteFactory = new FruiteFactory();
        Apple apple = (Apple)fruiteFactory.getApple();
        apple.setName("fushi");
        System.out.println(apple.getName());
    }
}
