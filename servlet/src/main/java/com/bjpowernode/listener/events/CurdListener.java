package com.bjpowernode.listener.events;

public class CurdListener implements IListener {
    @Override
    public void handle(ICurdEvent event) {
        /*不同事件类型，不同反应*/
        String eventType = event.getEventType();
        if (ICurdEvent.C_EVENT.equals(eventType)){/*事件类型为添加*/
            System.out.println("执行了 添加 操作");
        }else if (ICurdEvent.D_EVENT.equals(eventType)){
            System.out.println("执行了 删除 操作");
        }else if (ICurdEvent.U_EVENT.equals(eventType)){
            System.out.println("执行了 修改 操作");
        }else if(ICurdEvent.R_EVENT.equals(eventType)){
            System.out.println("执行了 回退 操作");
        }
    }
}
