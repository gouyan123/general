package com.bjpowernode.listener.events;

public class CurdEvent implements ICurdEvent {
    /*声明事件类型*/
    public final static String C_EVENT = "create event";
    public final static String D_EVENT = "delete event";
    public final static String U_EVENT = "update event";
    public final static String R_EVENT = "retrieve event";

    private IListenerable eventSource;/*事件源*/
    private String methodName;/*事件源所执行的方法名称*/

    //public CurdEvent(){}/*无参构造，用于spring容器创建bean*/

    public CurdEvent(IListenerable eventSource,String methodName){
        this.eventSource = eventSource;
        this.methodName = methodName;
    }

    @Override/*在创建事件对象的时候，传入事件源对象*/
    public IListenerable getEventSource() {
        return this.eventSource;
    }
    /*事件源执行的是哪个方法，也需要传进来
     *根据事件源执行的不同方法，返回不同的事件类型
     * */
    @Override
    public String getEventType() {
        String eventType = null;
        if (this.methodName.startsWith("save")){
            eventType = C_EVENT;
        }else if (this.methodName.startsWith("del")){
            eventType = D_EVENT;
        }else if (this.methodName.startsWith("update")){
            eventType = U_EVENT;
        }else if (this.methodName.startsWith("ret")){
            eventType = R_EVENT;
        }else {
            eventType = "have not this type";
        }
        return eventType;
    }
}
