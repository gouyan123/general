package com.listener;

public class Listener {
    public void handleNotify(Event event){
        if(event.getState().equals("click")){
            System.out.println("单击鼠标");
        }else if (event.getState().equals("move")){
            System.out.println("移动鼠标");
        }
    }
}
