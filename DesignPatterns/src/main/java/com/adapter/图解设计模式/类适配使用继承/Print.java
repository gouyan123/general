package com.adapter.图解设计模式.类适配使用继承;

public interface Print {
    /*调用老类的showWithParen()方法*/
    public abstract void printWeak();
    /*调用老类的showWithAster()方法*/
    public abstract void printStrong();
    public abstract void printOrigin();
}
