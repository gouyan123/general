package com.proxy;

import java.util.Random;

public class Tank implements Moveable{
    @Override
    public void move() {/*坦克移动后休眠若干秒*/
        try {

            System.out.println("tank move ...");
            Thread.sleep(new Random().nextInt(10000));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
