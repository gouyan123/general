package com.factory.func;

public class FactoryTest {
    public static void main(String[] args) {
        //System.out.println(new Factory().getMilk(););
        //货比三家
        //不知道谁好谁好谁坏
        //配置，可能会配置错
        Factory factory = new MengniuFactory();
        System.out.println(factory.getMilk());
    }
}
