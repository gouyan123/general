package com.proxy;

/*Tank2是Tank的代理，调用Tank2的任何方法，Tank2都会调用Tank的方法*/
public class Tank2 extends Tank {
    @Override
    public void move() {
        long start = System.currentTimeMillis();
        super.move();
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));
    }
}
