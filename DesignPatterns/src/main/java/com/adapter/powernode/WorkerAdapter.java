package com.adapter.powernode;

//适配器
public class WorkerAdapter implements IWorkerAdapter{
    private String workContent;
    @Override
    public String work(Object worker) {
        if (worker instanceof ICook){
            this.workContent = ((ICook)worker).cook();
        }
        if (worker instanceof IDrive){
            this.workContent = ((IDrive)worker).drive();
        }
        return this.workContent;
    }

    @Override
    public boolean supports(Object worker) {

        return false;
    }
}
