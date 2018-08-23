package com.bjpowernode.listener.events;

/*事件源类*/
public class Listenerable implements IListenerable {
    private IListener listener;

    @Override/*注册监听器*/
    public void setListener(IListener listener) {
        this.listener = listener;
    }

    @Override
    public void triggerListener(ICurdEvent event) {
        this.listener.handle(event);
    }
    /*事件源类中真正的业务逻辑，监听器监听的就是这些业务方法的执行*/
    /*被监听方法中调用监听方法，调用前创建事件对象event*/
    public void saveStudent(){
        System.out.println("向 DB 中插入一条数据");
        /*当执行这个方法时，通过事件触发监听器*/

        /*获得事件*/
        ICurdEvent curdEvent = new CurdEvent(this,"saveStudent");
        /*触发监听器*/
        this.triggerListener(curdEvent);

    }
    public void delStudent(){
        ICurdEvent curdEvent = new CurdEvent(this,"delStudent");
        this.triggerListener(curdEvent);
    }
    public void updateStudent(){
        ICurdEvent curdEvent = new CurdEvent(this,"updateStudent");
        this.triggerListener(curdEvent);
    }
}
