package com.factory.abstractFactory;

import com.factory.product.Mengniu;
import com.factory.product.Milk;
import com.factory.product.Telunsu;
import com.factory.product.Yili;

public class MilkFactory extends  AbstractFactory {
    @Override
    public Milk getMengniu() {
        return new Mengniu();
    }
    @Override
    public Milk getYili() {
        return new Yili();
    }
    @Override
    public Milk getTelunsu() {
        return new Telunsu();
    }
}
