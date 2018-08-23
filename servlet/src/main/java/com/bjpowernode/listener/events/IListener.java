package com.bjpowernode.listener.events;

/*监听接口*/
public interface IListener {
    /*监听接口处理增删改查事件*/
    public abstract void handle(ICurdEvent event);
}
