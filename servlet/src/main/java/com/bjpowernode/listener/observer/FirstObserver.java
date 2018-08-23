package com.bjpowernode.listener.observer;

public class FirstObserver implements IObserver {
    @Override
    public void handleNotify(String message) {
        System.out.println("I am FirstObserver,handing message " + message);
    }
}
