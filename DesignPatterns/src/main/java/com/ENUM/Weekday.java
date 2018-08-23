package com.ENUM;

public enum Weekday {
    /* 第 1 行：必须定义全局常量对象，每个常量对象都是 Weekday类型的，就是定义几个现成可用的类对象*/
    /* MONDAY 等价于 private static final Weekday MONDAY = new Weekday(变量名,序号);*/
    MONDAY(99999),TUESDAY(2),WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY;
    /*下面代码 正常按类进行定义 即可*/
    private int index;
    /*有参构造*/
    private Weekday(int index){
        this.index = index;
    }
    /*无参构造*/
    private Weekday(){

    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    /*测试：定义main方法进行测试*/

    public static void main(String[] args) {
        /*--------------------枚举类主要方法--------------------*/
        String name = Weekday.FRIDAY.name();
        System.out.println("全局常量 name = " + name);
        int index = Weekday.FRIDAY.ordinal();
        System.out.println("全局常量 index = " + index);
        /*获得该枚举类中 所有全局常量*/
        Weekday[] weekdays = Weekday.values();
        for (Weekday weekday : weekdays){
            System.out.println("name = " + weekday.name() + "----- index = " + weekday.ordinal());
        }
        /*--------------------测试构造方法保存值--------------------*/
        index = Weekday.MONDAY.getIndex();
        System.out.println("MONDAY对象中封装的值 index = " + index);
    }
}
