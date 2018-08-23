package com.adapter.powernode;

import java.util.ArrayList;
import java.util.List;

public interface IWorkerAdapter {
    public static final List<Object> adapters = new ArrayList<Object>();
    //                        用于接收参数，根据参数类型，执行相应方法
    public abstract String work(Object worker);
    public abstract boolean supports(Object worker);
}
