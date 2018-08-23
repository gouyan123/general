package com.strategy.pay.payport;

import com.strategy.pay.PayState;

public interface Payment {
    public PayState pay(String uid, double amount);

}
