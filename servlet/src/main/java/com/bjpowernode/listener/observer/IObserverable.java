package com.bjpowernode.listener.observer;

/*定义被观察者接口，“可被观察的”*/
public interface IObserverable {
    /*添加观察者*/
    public abstract void addObserver(IObserver observer);

    /*删除观察者*/
    public abstract void removeObserver(IObserver observer);

    /*通知观察者*/
    public abstract void notifyObservers(String message);
}
