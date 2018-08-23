package com.listener;

public class Test {
    public static void main(String[] args) {
        /**创建事件源，即被监听器*/
        EventSource eventSource = new EventSource();
        /**创建事件源的监听器*/
        Listener listener = new Listener();
        /**将监听器注册到事件源（被监听器上）*/
        eventSource.setListener(listener);
        /**调用事件源（被监听器）的业务方法*/
        eventSource.click();/*被监听器中的被监听方法click（业务方法）调用监听器中的监听方法handleNotify()*/
        eventSource.move();
    }
}
