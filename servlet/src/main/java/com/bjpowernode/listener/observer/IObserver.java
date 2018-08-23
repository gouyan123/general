package com.bjpowernode.listener.observer;

/*定义观察者接口*/
public interface IObserver {
    /*观察者在收到通知后做出相应处理*/
    public abstract void handleNotify(String message);
}
