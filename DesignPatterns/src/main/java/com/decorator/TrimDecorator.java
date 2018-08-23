package com.decorator;

/*装饰者子类继承装饰者基类，并覆写目标类业务方法*/
public class TrimDecorator extends SomeServiceWrapper {
    private ISomeService someService;
    public TrimDecorator(ISomeService someService) {
        super(someService);
        this.someService = someService;
    }
    @Override
    public String doSome() {
        return super.doSome().trim();
    }
}
