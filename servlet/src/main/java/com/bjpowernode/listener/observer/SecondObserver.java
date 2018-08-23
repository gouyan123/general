package com.bjpowernode.listener.observer;

public class SecondObserver implements IObserver {
    @Override
    public void handleNotify(String message) {
        System.out.println("I am SecondObserver,handing message " + message);
    }
}
