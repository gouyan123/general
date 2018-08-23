package com.bjpowernode.filter.decorator;

/*具体装饰者：
* 1、要继承自装饰者基类
* 2、要有带参构造器
* 3、具体装饰者只对装饰者基类业务方法进行某一种单一的增强
* */
public class TrimDecorator extends SomeServiceWrapper {
    public TrimDecorator() {
    }

    public TrimDecorator(ISomeService target) {
        super(target);
    }

    /*重写基类的业务方法，并增强*/
    @Override
    public String doSome() {
        /*调用基类业务方法，并增强*/
        return super.doSome().trim();
    }
}
