package com.dongnao.mark.base;

import com.dongnao.mark.base.threadstate.SleepUtils;

/**守护线程：
 * 设置线程为守护线程：thread.setDaemon(true);
 * finnally块中代码不一定会执行*/
public class Daemon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner());
        /**设置线程为守护线程*/
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
                SleepUtils.second(100);
            } finally {
                /** finnally块中代码不一定会执行*/
                System.out.println("DaemonThread finally run.");
            }
        }
    }
}
