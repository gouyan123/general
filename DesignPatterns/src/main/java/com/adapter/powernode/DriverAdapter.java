package com.adapter.powernode;

public class DriverAdapter implements IWorkerAdapter {
    @Override
    public String work(Object worker) {
        return ((IDrive)worker).drive();
    }

    @Override
    public boolean supports(Object worker) {
        return (worker instanceof IDrive);
    }
}
