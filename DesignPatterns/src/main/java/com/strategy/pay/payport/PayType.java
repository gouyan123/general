package com.strategy.pay.payport;

public enum PayType {
    /* ALI_PAY,WECHAT_PAY,UNION_PAY,JD_PAY 都是 PayType类的对象 */
    /* ALI_PAY 等价于 private static final PayType ALI_PAY = new PayType(...);*/
    ALI_PAY(new AliPay()),
    WECHAT_PAY(new WechatPay()),
    UNION_PAY(new UnionPay()),
    JD_PAY(new JDPay());
    private Payment payment;
    PayType(Payment payment){
        this.payment = payment;
    }
    public Payment getPayment(){ return  this.payment;}
}
