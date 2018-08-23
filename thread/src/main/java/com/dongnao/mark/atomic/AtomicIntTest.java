package com.dongnao.mark.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntTest {
    static AtomicInteger ai = new AtomicInteger(1);
    public static void main(String[] args) {
        Integer x = ai.getAndIncrement();
        Integer y = ai.get();
        Integer z = ai.incrementAndGet();
        System.out.println("x = " + x + ";" + "y = " + y + ";" + "z = " + z + ";");
    }
}
