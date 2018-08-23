package com.adapter.图解设计模式.类适配使用继承;

public class Test {
    public static void main(String[] args) {
        /*创建新的类*/
        PrintBanner printBanner = new PrintBanner("Hello");
        /*调用新的需求接口的方法*/
        printBanner.printWeak();
        printBanner.printStrong();
        printBanner.printOrigin();
    }
}
