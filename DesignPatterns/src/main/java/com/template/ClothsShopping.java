package com.template;

public class ClothsShopping extends Shopping {
    @Override
    public void buy() {
        System.out.println("买衣服");
    }

    @Override
    public void pay() {
        System.out.println("支付宝支付");
    }
}
