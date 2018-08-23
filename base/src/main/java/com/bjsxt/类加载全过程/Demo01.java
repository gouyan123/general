package com.bjsxt.类加载全过程;

public class Demo01 {
    public static void main(String[] args) {
//        String str = "aaa";
//        int a = 323432;
        /*要使用一个类，必须先 加载 链接(给静态资源分配空间) 初始化(给静态变量赋值并执行静态块) 使用 销毁*/
        A a = new A();
        System.out.println(A.width);
    }
}

class A {
    public static int width = 100;
    static {
        System.out.println("静态初始化类A");
        width = 300;
    }
    public A(){
        System.out.println("创建A类对象");
    }
}
