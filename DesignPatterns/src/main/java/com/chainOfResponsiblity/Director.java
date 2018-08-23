package com.chainOfResponsiblity;

public class Director extends Leader {

    public Director(String name) {
        super(name);
    }

    @Override
    public void handlerRequest(LeaveRequest leaveRequest) {
        if (leaveRequest.getLeaveDays() >= 3){
            System.out.println("大于等于3天，Derectory批准");
        }else {
            if (this.getNextLeader() != null){
                this.getNextLeader().handlerRequest(leaveRequest);
            }
        }
    }
}
