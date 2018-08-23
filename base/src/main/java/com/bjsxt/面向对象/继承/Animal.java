package com.bjsxt.面向对象.继承;

public class Animal {
    public void voice(){
        System.out.println("动物都会叫！");
    }
    public static void testAnimalVoice(Animal a){
        /**a：父类引用 a 指向子类对象 cat，调用Cat类方法voice()*/
        a.voice();
        if (a instanceof Animal){
            /**父类中没有catchMouse()方法，需要向下转型为Cat类型，才能调到catchMouse()方法*/
            ((Cat)a).catchMouse();
        }
    }
    public static void main(String[] args) {
        Animal a = new Cat();
        Cat a2 = (Cat)a;
        testAnimalVoice(a);
    }
}

class Cat extends Animal{
    @Override
    public void voice() {
        System.out.println("喵喵喵！");
    }
    public void catchMouse(){
        System.out.println("抓老鼠");
    }
}

class Dog extends Animal{
    @Override
    public void voice() {
        System.out.println("汪汪汪！");
    }
}
