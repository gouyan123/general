package com.adapter.图解设计模式.类适配使用继承;

public class PrintBanner extends Banner implements Print {
    public PrintBanner(String slogan) {
        super(slogan);
    }

    @Override
    public void printWeak() {
        super.showWithParen();
    }

    @Override
    public void printStrong() {
        super.showWithAster();
    }

    @Override
    public void printOrigin() {
        System.out.println(super.getSlogan());
    }
}
