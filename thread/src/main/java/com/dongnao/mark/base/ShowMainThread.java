package com.dongnao.mark.base;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ShowMainThread {
    public static void main(String[] args) {
        /*jvm虚拟机线程管理接口*/
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        /*不考虑线程的监视器跟锁信息，只拿线程跟线程的堆栈信息*/
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false,false);
        for(ThreadInfo threadInfo:threadInfos){
            System.out.println("id : " + threadInfo.getThreadId() + " - " + "name : " + threadInfo.getThreadName());
        }
    }
}
