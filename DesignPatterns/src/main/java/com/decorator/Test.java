package com.decorator;

public class Test {
    public static void main(String[] args) {
        String result = null;
        /*目标类增强前*/
        ISomeService someService = new SomeServiceImpl();
        result = someService.doSome();
        System.out.println(result);
        /*使用TrimDecorator装饰子类进行增强*/
        ISomeService trimDecorator = new TrimDecorator(someService);
        result = trimDecorator.doSome();
        System.out.println(result);
        /*使用ToUpperCaseDecorator装饰子类进行增强*/
        /*重点：对上一个修饰子类进行修饰，形成修饰链*/
        ISomeService upperCaseDecorator = new ToUpperCaseDecorator(trimDecorator);
        result = upperCaseDecorator.doSome();
        System.out.println(result);
        ISomeService xYZDecorator = new XYZDecorator(upperCaseDecorator);
        result = xYZDecorator.doSome();
        System.out.println(result);
    }
}
