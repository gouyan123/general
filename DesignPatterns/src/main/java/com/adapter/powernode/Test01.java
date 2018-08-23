package com.adapter.powernode;

public class Test01 {
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
        IWorkerAdapter workerAdapter = new WorkerAdapter();
        for (Object work : works){
            String result = workerAdapter.work(work);
            System.out.println(result);
        }
    }
}
