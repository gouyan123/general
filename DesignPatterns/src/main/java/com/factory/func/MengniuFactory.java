package com.factory.func;

import com.factory.product.Mengniu;
import com.factory.product.Milk;

public class MengniuFactory implements  Factory {
    @Override
    public Milk getMilk() {
        return new Mengniu();
    }
}
