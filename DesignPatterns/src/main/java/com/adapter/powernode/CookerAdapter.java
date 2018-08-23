package com.adapter.powernode;

public class CookerAdapter implements IWorkerAdapter {
    @Override
    public String work(Object worker) {
        return ((ICook)worker).cook();
    }

    @Override
    public boolean supports(Object worker) {
        return (worker instanceof ICook);
    }
}
