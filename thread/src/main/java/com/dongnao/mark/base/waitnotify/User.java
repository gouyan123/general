package com.dongnao.mark.base.waitnotify;

public class User {
    private  Integer age;
    private String city;
    public User() {
    }
    public User(Integer age,String city){
        this.age = age;
        this.city = city;
    }

    public synchronized void changeAge(Integer age){
        this.age = age;
        notifyAll();

    }
    public synchronized void changeCity(String city){
        this.city = city;
        /*当前线程通过 synchronized 获得锁，此处通过 notify 释放锁，唤醒在当前对象上等待的线程*/
        this.notifyAll();
        System.out.println("-----------changeCity");
    }
    public synchronized void waitAge() {
        while (this.age <= 30){
            try {
                this.wait();
                System.out.println("waitAge : " + Thread.currentThread().getId() + "is notified");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("the age is " + this.age);
    }
    public synchronized void waitCity() {
        while (this.city.equals("NewYork")){
            try {
                this.wait();
                System.out.println("waitCity : " + Thread.currentThread().getId() + "is notified");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("the city is " + this.city);
    }
}
class TestUser{
    private static User user = new User(29,"NewYork");
    /*private static User user2 = new User(31,"NewYork");*/
    private static class CheckAge implements Runnable{
        @Override
        public void run() {
            user.waitAge();
        }
    }
    private static class CheckCity implements Runnable{
        @Override
        public void run() {
            user.waitCity();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0;i < 3;i++){
            new Thread(new CheckAge()).start();
        }
        for (int i = 0;i < 3;i++){
            new Thread(new CheckCity()).start();
        }
        /**6个线程操作同一个 user 对象，只有一个线程能获得 user对象的锁*/
        Thread.sleep(1000);
        user.changeCity("London");
    }
}
