package com.chainOfResponsiblity;

public class Manager extends Leader {

    public Manager(String name) {
        super(name);
    }

    @Override
    public void handlerRequest(LeaveRequest leaveRequest) {
        if (leaveRequest.getLeaveDays() < 3){
            System.out.println("小于3天，Manager批准");
        }else {
            if (this.getNextLeader() != null){
                this.getNextLeader().handlerRequest(leaveRequest);
            }
        }
    }
}
