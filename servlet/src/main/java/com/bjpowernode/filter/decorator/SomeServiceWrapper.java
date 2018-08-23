package com.bjpowernode.filter.decorator;

/*装饰者基类：
* 1、做为父类，必须有无参构造方法；
* 2、不做任何增强
* */
public class SomeServiceWrapper implements ISomeService {
    private ISomeService target;/*目标对象*/

    public SomeServiceWrapper() {
    }

    public SomeServiceWrapper(ISomeService target) {
        this.target = target;
    }

    @Override
    public String doSome() {
        return this.target.doSome();
    }
}
