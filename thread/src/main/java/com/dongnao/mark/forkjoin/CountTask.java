package com.dongnao.mark.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask {
    private static final int THRESHOLD = 2;
    private int start;
    private int end;
    public CountTask(int start,int end){
        this.start = start;
        this.end = end;
    }
    @Override
    protected Object compute() {
        Integer sum = 0;
        boolean canCompute;
        /*任务足够小就执行业务逻辑，否则再拆分*/
        canCompute = (end - start) < THRESHOLD;
        if (canCompute){
            for (int i=start;i<=end;i++){
                sum = sum + i;
            }
        }else {
            /*任务大于阈值，再分*/
            Integer mid = (start + end)/2;
            CountTask left = new CountTask(start,mid);
            CountTask right = new CountTask(mid+1,end);
            /*fork()：把当前任务放到ForkJoinTask数组queue里面，然后再调用
            ForkJoinPool的signalWork()方法唤醒或创建一个工作线程来执行任务*/
            left.fork();
            right.fork();
            /*join()方法的主要作用是阻塞当前线程并等待获取结果。首先通过查看
            任务的状态，看任务是否已经执行完了，如果执行完了，则直接返回任务
            状态，如果没有执行完，则从任务数组里取出任务并执行。如果任务顺利
            执行完成了，则设置任务状态为NORMAL，如果出现异常，则纪录异常，并
            将任务状态设置为EXCEPTIONAL。*/
            Object leftResult = left.join();
            Object rightReslt = right.join();
            //合并子任务
            sum = (Integer) leftResult  + (Integer) rightReslt;
        }
        return sum;
    }

    public static void main(String[] args) {
        /*创建forkJoinPool池，里面存ForkJoinTask数组和ForkJoinWorkerThread数组*/
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        /*生成一个计算任务，负责计算1+2+3+4*/
        CountTask task =new CountTask(1, 5);
        /*向池中加人任务对象*/
        Future result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
