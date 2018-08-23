package com.dongnao.mark.completionService;

import java.util.Random;
import java.util.concurrent.Callable;

public class WorkTask implements Callable<String>{
    private String threadName;
    public WorkTask() {
    }
    public WorkTask(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public String call() throws Exception {
        /*定义任务线程随机休眠时间，表示线程执行时间*/
        int sleepTime = new Random().nextInt(5000);
        Thread.sleep(sleepTime);
        String str = this.threadName + "work time : " + sleepTime;
        System.out.println(str + " finished");
        return str;
    }
}
