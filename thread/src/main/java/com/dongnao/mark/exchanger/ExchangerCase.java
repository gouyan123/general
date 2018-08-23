package com.dongnao.mark.exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class ExchangerCase {
    /*List<String>为两个线程执行exchange()方法时，交换的内容*/
    private static final Exchanger<List<String>> exgr = new Exchanger<List<String>>();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<String>();
                list.add(Thread.currentThread().getName() + "insert A1");
                list.add(Thread.currentThread().getName() + "insert A2");
                try {
                    /*线程阻塞在exchange(交换内容)方法，直到另一个线程执行exchange(交换内容)方法*/
                    list = exgr.exchange(list);
                    /*查看 交换内容list是否发生变化*/
                    for (String str : list){
                        System.out.println(Thread.currentThread().getName() + ":" + str);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<String>();
                list.add(Thread.currentThread().getName() + "insert B1");
                list.add(Thread.currentThread().getName() + "insert B2");
                list.add(Thread.currentThread().getName() + "insert B3");
                try {
                    /*线程阻塞在exchange(交换内容)方法，直到另一个线程执行exchange(交换内容)方法*/
                    list = exgr.exchange(list);
                    /*查看 交换内容list是否发生变化*/
                    for (String str : list){
                        System.out.println(Thread.currentThread().getName() + ":" + str);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
    }
}
