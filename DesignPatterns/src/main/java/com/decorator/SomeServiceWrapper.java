package com.decorator;

/*定义装饰者基类，与目标类实现同一个接口或继承同一个父类*/
public class SomeServiceWrapper implements ISomeService {
    /*装饰者基类持有目标类，作为成员变量*/
    private ISomeService someService;

    public SomeServiceWrapper(ISomeService someService) {
        this.someService = someService;
    }

    @Override
    public String doSome() {
        /*装饰者基类在业务方法中调用目标类的业务方法，但是不做任何增强，由一个个装饰者子类去增强*/
        return this.someService.doSome();
    }
}
