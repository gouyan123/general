package com.adapter.图解设计模式.类适配使用继承;

public class Banner {
    private String slogan;/*标语*/
    public Banner(String slogan) {
        this.slogan = slogan;
    }
    public String getSlogan() {
        return slogan;
    }
    public void showWithParen(){
        System.out.println("(" + this.slogan + ")");
    }
    public void showWithAster(){
        System.out.println("*" + this.slogan + "*");
    }
}
