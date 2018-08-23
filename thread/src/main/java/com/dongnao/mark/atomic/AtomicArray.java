package com.dongnao.mark.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArray {
    public static void main(String[] args) {
        int[] values = new int[]{2,4,6};
        AtomicIntegerArray aia = new AtomicIntegerArray(values);
        aia.getAndSet(0,20);
        System.out.println(aia.get(0));
        System.out.println(values[0]);//不会改变原数组
    }
}
