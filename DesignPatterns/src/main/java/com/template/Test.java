package com.template;

public class Test {
    public static void main(String[] args) {
        Shopping shoes = new ShoesShopping();
        Shopping clothes = new ClothsShopping();
        shoes.buyGoods();
        System.out.println("==========================");
        clothes.buyGoods();
    }
}
