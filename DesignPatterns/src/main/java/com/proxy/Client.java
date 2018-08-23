package com.proxy;

public class Client {
    public static void main(String[] args) throws Exception {
        /*创建目标类对象*/
        Tank t = new Tank();
        /*创建代理类对象，假设此时Proxy已经可以创建代理对象了*/
    /*TankTimeProxy ttp = (TankTimeProxy)Proxy.newProxyInstance();
    Moveable m = ttp;*/
        /*上面两句简写如下*/
        Moveable m = (Moveable)Proxy.newProxyInstance(Moveable.class,new TimeHandler(new Tank()));
        m.move();
        /*假如Proxy可以生成动态代理 m，就可以这么调用*/
    }
}
