package com.bjpowernode.filter.decorator;

public class ToUpcaseDecorator extends SomeServiceWrapper {
    public ToUpcaseDecorator() {
        super();
    }
    public ToUpcaseDecorator(ISomeService target){
        super(target);
    }

    @Override
    public String doSome() {
        return super.doSome().toUpperCase();
    }
}
