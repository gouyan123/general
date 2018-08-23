package com.adapter.图解设计模式.对象适配使用委托;

public class PrintBanner implements Print {
    private Banner banner;/*使用聚合*/
    public PrintBanner(Banner banner) {
        this.banner = banner;
    }

    @Override
    public void printWeak() {
        /*委托：一个类中的方法调用了另一个类的方法，不自己处理问题，就属于委托*/
        this.banner.showWithParen();
    }

    @Override
    public void printStrong() {
        /*委托：一个类中的方法调用了另一个类的方法，不自己处理问题，就属于委托*/
        this.banner.showWithAster();
    }

    @Override
    public void printOrigin() {
        System.out.println(this.banner.getSlogan());
    }
}
