package com.proxy;

/*目标类 Tank 的静态代理类 TankTimeProxy*/
public class TankTimeProxy implements Moveable {
    private Moveable t;/*目标类tank*/
    public TankTimeProxy(Moveable t) {
        this.t = t;
    }
    @Override
    public void move() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("start at : " + start);
        this.t.move();
        long end = System.currentTimeMillis();
        System.out.println("end at : " + end);
        System.out.println("spend : " + (end - start));
    }
}
