package com.chainOfResponsiblity;

public abstract class Leader {
    private String name;
    private Leader nextLeader;

    public abstract void handlerRequest(LeaveRequest leaveRequest);

    public Leader(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNextLeader(Leader nextLeader) {
        this.nextLeader = nextLeader;
    }

    public String getName() {
        return name;
    }

    public Leader getNextLeader() {
        return nextLeader;
    }
}
