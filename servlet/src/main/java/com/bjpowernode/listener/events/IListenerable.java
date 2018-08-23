package com.bjpowernode.listener.events;

/*事件源接口(可被观察，可被监听)*/
public interface IListenerable {
    /*为数据源注册监听器*/
    public abstract void setListener(IListener listerer);
    /*事件源靠事件触发监听器*/
    public abstract void triggerListener(ICurdEvent event);
}
