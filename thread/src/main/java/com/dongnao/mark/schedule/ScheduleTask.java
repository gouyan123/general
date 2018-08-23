package com.dongnao.mark.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.FutureTask;

public class ScheduleTask implements Runnable{
    FutureTask future;
    public static enum OperType{
        None,
        OnlyThrowException,
        CacheException
    }

    private OperType operType;

    public ScheduleTask() {
    }

    public ScheduleTask(OperType operType){
        this.operType = operType;
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        switch (this.operType){
            case OnlyThrowException:
                System.out.println("Exception not Catche! " + this.sdf.format(new Date()));
                throw new RuntimeException("OnlyThrowException");
            case CacheException:
                try {
                    throw new RuntimeException("CacheException");
                }catch (RuntimeException e){
                    System.out.println("Exception is Caught! " + this.sdf.format(new Date()));
                }
                break;/*终止线程*/
            case None:
                System.out.println("None! " + this.sdf.format(new Date()));
        }
    }
}
