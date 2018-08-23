package com.adapter.powernode;

import java.util.ArrayList;
import java.util.List;

//为每个工种设置一个适配器
public class Test02 {
    public static void main(String[] args) {
        //运行的语句中一般用接口，不用类
        //类主要是编译，接口主要用于运行
        ICook cook = new ICook() {
            @Override
            public String cook() {
                return "做饭";
            }
        };
        IDrive drive = new IDrive() {
            @Override
            public String drive() {
                return "开车";
            }
        };
        //适配器实现循环遍历
        Object[] works = new Object[]{cook,drive};
        //IWorkerAdapter workerAdapter = new WorkerAdapter();
        for (Object work : works){
            IWorkerAdapter adapter = getAdapter(work);
            String result = adapter.work(work);
            System.out.println(result);
        }
    }
    //根据参数确定对应适配器对象
    private static IWorkerAdapter getAdapter(Object work) {
        //假设通过getAllAdapters()方法获取所有适配器
        List<IWorkerAdapter> adapters = getAllAdapters();
        for (IWorkerAdapter adapter : adapters){
            if (adapter.supports(work)){
                return adapter;
            }
        }
        return null;
    }
    //获取所有适配器
    public static List<IWorkerAdapter> getAllAdapters() {
        List<IWorkerAdapter> adapters = new ArrayList<IWorkerAdapter>();
        adapters.add(new CookerAdapter());
        adapters.add(new DriverAdapter());
        return adapters;
    }
}
