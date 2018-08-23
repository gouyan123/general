package com.chainOfResponsiblity;

public class LeaveRequest {
    private String empName;
    private int leaveDays;

    public LeaveRequest(String empName, int leaveDays) {
        this.empName = empName;
        this.leaveDays = leaveDays;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setLeaveDays(int leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getEmpName() {
        return empName;
    }

    public int getLeaveDays() {
        return leaveDays;
    }
}
