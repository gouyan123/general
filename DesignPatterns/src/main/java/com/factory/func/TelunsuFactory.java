package com.factory.func;

import com.factory.product.Milk;
import com.factory.product.Telunsu;

/**
 * 事情变得越来越专业
 * Created by Tom on 2018/3/4.
 */
public class TelunsuFactory implements Factory {
    @Override
    public Milk getMilk() {
        return new Telunsu();
    }
}
