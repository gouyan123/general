package com.bjpowernode.listener.observer;

import java.util.ArrayList;
import java.util.List;

public class Some implements IObserverable {
    private List<IObserver> observers;
    public Some(){
        this.observers = new ArrayList<IObserver>();
    }
    @Override
    public void addObserver(IObserver observer) {
        System.out.println("add observer");
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        System.out.println("remove observer");
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (IObserver observer : observers) {
            observer.handleNotify(message);
        }
    }
}
