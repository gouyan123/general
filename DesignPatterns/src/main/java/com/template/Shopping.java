package com.template;

//父类
public abstract class Shopping {

    //模板方法
    public void buyGoods(){
        userLogin();
        buy();
        pay();
    }

    public final void userLogin() {
        System.out.println("用户登录");
    }
    //必须子类实现
    public abstract void buy();
    //钩子方法：子类可重写，也可不重写
    public void pay(){
        System.out.println("使用银联支付");
    }
}
