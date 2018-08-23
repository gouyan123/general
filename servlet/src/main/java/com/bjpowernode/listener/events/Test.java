package com.bjpowernode.listener.events;

/*被监听者调用监听者监听方法*/
public class Test {
    public static void main(String[] args) {
        /*创建事件源，及被监听者*/
        Listenerable listenerable = new Listenerable();
        /*创建监听者*/
        IListener curdListener = new CurdListener();
        /*将监听者注册到被监听者中*/
        listenerable.setListener(curdListener);
        listenerable.saveStudent();
        listenerable.updateStudent();
    }
}
