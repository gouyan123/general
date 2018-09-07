package com.lamda;

public class LamdaTest {
    public static void main(String[] args) {
        CalculateInterface cal01 = new CalculateInterface() {
            @Override
            public int sum(int x, int y) {
                return x + y;
            }
        };
        System.out.println("匿名内部类写法 " + cal01.sum(1,2));

        CalculateInterface cal02 = (int x,int y) -> {
            return x + y;
        };
        System.out.println("Lamda表达式写法 " + cal02.sum(1,2));

        /*简写，参数数据类型可省略，return返回数据时return可省略，方法体中只有一句时可省略{}*/
        CalculateInterface cal03 = (x,y) -> x + y;
        System.out.println("Lamda表达式写法 " + cal03.sum(1,2));

        /*匿名内部类使用访问外层作用域的变量，则外层变量会自动被编译为 final*/
    }
}
