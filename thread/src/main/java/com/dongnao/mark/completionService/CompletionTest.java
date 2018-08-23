package com.dongnao.mark.completionService;

import java.util.concurrent.*;

public class CompletionTest {
    Executor e;

    private final int POOL_SIZE = 5;
    private final int TOTAL_TASK = 10;
    public void testByQueue() throws InterruptedException, ExecutionException {
        /*利用Executors工厂类创建线程池执行器*/
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        BlockingQueue<Future<String>> queue = new LinkedBlockingQueue<Future<String>>();
        /*放任务*/
        for (int i=0;i<TOTAL_TASK;i++){
            /*执行器提交任务（Runnable接口对象和Callable接口对象），并执行任务，返回结果 Future*/
            Future<String> future = pool.submit(new WorkTask("Execute task gy_" + i));
            queue.add(future);
        }
        /*拿结果*/
        for (int i=0;i<TOTAL_TASK;i++){
            /*queue.take()弹出队列中第一个元素*/
            System.out.println("Execute task : " + queue.take().get());
        }

        /*关闭连接池*/
        pool.shutdown();
    }
    /*使用CompletionService*/
    public void testByCompletionService() throws InterruptedException, ExecutionException {
        /*利用Executors工厂类创建线程池执行器*/
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        CompletionService<String> service = new ExecutorCompletionService<String>(pool);
        /*放任务*/
        for (int i=0;i<TOTAL_TASK;i++){
            service.submit(new WorkTask("gy_" + i));
        }
        /*拿结果*/
        for (int i=0;i<TOTAL_TASK;i++){
            Future<String> future = service.take();
            /*queue.take()弹出队列中第一个元素*/
            System.out.println("Execute task : " + future.get());
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletionTest test = new CompletionTest();
        test.testByQueue();
    }
}
