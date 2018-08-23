package com.bjpowernode.listener.events;

/*定义增删改查事件*/
public interface ICurdEvent {
    /*声明事件类型*/
    public final static String C_EVENT = "create event";
    public final static String D_EVENT = "delete event";
    public final static String U_EVENT = "update event";
    public final static String R_EVENT = "retrieve event";
    /*通常,通过事件对象，获得事件源对象*/
    public abstract IListenerable getEventSource();
    /*获取事件类型：增，删，改，查*/
    public abstract String getEventType();
}
