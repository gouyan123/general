package com.factory.abstractFactory;

import com.factory.product.Milk;

/**抽象工厂是用户的主入口*/
public abstract class AbstractFactory {
    /**获得一个蒙牛品牌的牛奶*/
    public  abstract Milk getMengniu();

    /**获得一个伊利品牌的牛奶*/
    public abstract  Milk getYili();

    /**获得一个特仑苏品牌的牛奶*/
    public  abstract  Milk getTelunsu();
}
