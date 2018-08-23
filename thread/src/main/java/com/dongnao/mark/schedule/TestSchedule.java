package com.dongnao.mark.schedule;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestSchedule {
    public static void main(String[] args) {
        /*创建线程池*/
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        /*线程池向自己DelayQueue任务队列中添加任务，工作线程获取任务后延迟 1s 执行，然后每隔 5s 执行异常*/
        poolExecutor.scheduleAtFixedRate(new ScheduleTask(ScheduleTask.OperType.None),
                1000,5000, TimeUnit.MILLISECONDS);
        poolExecutor.scheduleAtFixedRate(new ScheduleTask(ScheduleTask.OperType.OnlyThrowException),
                1000,5000, TimeUnit.MILLISECONDS);
        poolExecutor.scheduleAtFixedRate(new ScheduleTask(ScheduleTask.OperType.CacheException),
                1000,5000, TimeUnit.MILLISECONDS);
        /*每隔 5s 执行一次，每次延迟 1s 执行*/
        poolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleWithFixedDelay start at : " + ScheduleTask.sdf.format(new Date()));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("scheduleWithFixedDelay end at : " + ScheduleTask.sdf.format(new Date()));
            }
        },1000,5000,TimeUnit.MILLISECONDS);
        /*只执行 1 次*/
        poolExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("schedule is Running!");
            }
        },1000,TimeUnit.MILLISECONDS);
    }
}
