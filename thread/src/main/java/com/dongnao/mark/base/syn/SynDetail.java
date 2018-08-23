package com.dongnao.mark.base.syn;

public class SynDetail {
    public static void main(String[] args) {
        /**锁 SynDetail.class 对象，多个线程操作这个对象时，只有一个能进入这个synchronized块*/
        synchronized (SynDetail.class){

        }
    }
}
