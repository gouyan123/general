package com.bjpowernode.listener.listener;
/*事件源(被监听者)：鼠标
* 事件：单击，双击，移动
* 监听者：去实现单击，双击，移动*/
public class EventSource {
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }
    public void notifyListener(Event event){
        this.listener.handleNotify(event);
    }

    public void click(){
        Event event = new Event(this,"click");
        this.notifyListener(event);
    }
    public void move(){
        Event event = new Event(this,"move");
        this.notifyListener(event);
    }
}
