package com.decorator;

import java.util.HashMap;
import java.util.Map;

/*装饰者子类继承装饰者基类，并覆写目标类业务方法*/
public class ToUpperCaseDecorator extends SomeServiceWrapper {
    private ISomeService someService;
    public ToUpperCaseDecorator(ISomeService someService) {
        super(someService);
        this.someService = someService;
    }

    @Override
    public String doSome() {
        Map map = new HashMap<>();
        return super.doSome().toUpperCase();

    }
}
