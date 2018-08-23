package com.proxy;
/*Tank3是Tank的代理，调用Tank3的任何方法，Tank3都会调用Tank的方法*/
public class Tank3 implements Moveable {
    private Moveable tank;

    public Tank3() {
    }

    public Tank3(Moveable tank) {
        this.tank = tank;
    }

    @Override
    public void move() throws Exception {
        long start = System.currentTimeMillis();
        this.tank.move();
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));
    }
}
