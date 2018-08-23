package com.dongnao.mark.future;

import java.util.concurrent.Callable;

public class ComputerTask implements Callable<Integer> {
    private Integer result = 0;
    private String taskName = "";

    public ComputerTask(Integer result, String taskName) {
        this.result = result;
        this.taskName = taskName;
        System.out.println(this.taskName + " 子任务已创建");
    }

    @Override
    public Integer call() throws Exception {
        for (int i=0;i<100;i++){
            this.result += i;
        }
        Thread.sleep(10000);
        System.out.println(this.taskName + " 子任务已完成");
        return this.result;
    }
}
