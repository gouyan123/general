package com.adapter.图解设计模式.对象适配使用委托;

public class Test {
    public static void main(String[] args) {
        Banner banner = new Banner("Hello!!!");
        PrintBanner printBanner = new PrintBanner(banner);
        printBanner.printWeak();
        printBanner.printStrong();
        printBanner.printOrigin();
    }
}
