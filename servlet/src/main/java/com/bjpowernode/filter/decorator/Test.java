package com.bjpowernode.filter.decorator;

public class Test {
    public static void main(String[] args){
        /*创建目标对象*/
        ISomeService target = new SomveServiceImpl();
        /*以目标对象为参数，创建修饰者*/
        ISomeService service = new TrimDecorator(target);
        /*第一次增强的结果作为第二次增强的参数出现，形成装饰者链*/
        ISomeService service2 = new ToUpcaseDecorator(service);
        /*使用装饰者的业务方法*/
        String result = service2.doSome();
        System.out.println("result = " + result);
    }
}
