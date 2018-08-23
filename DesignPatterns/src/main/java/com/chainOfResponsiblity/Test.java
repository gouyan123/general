package com.chainOfResponsiblity;

public class Test {
    public static void main(String[] args) {
        Leader maneger = new Manager("zhangsan");
        Leader director = new Director("lisi");
        /*组织责任链对象的关系，可以定义到配置文件中，通过反射实现*/
        maneger.setNextLeader(director);
        /*开始请假操作*/
        LeaveRequest leaveRequest = new LeaveRequest("gy",5);
        maneger.handlerRequest(leaveRequest);
    }
}
