package com.bjsxt.syn;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

/**
 * 线程安全：操作并发容器
 * 
 * @author 裴新 QQ:3401997271
 *
 */
public class SynContainer {
	public static void main(String[] args) throws InterruptedException {
		CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
		for(int i=0;i<10000;i++) {
			new Thread(()->{
				list.add(Thread.currentThread().getName());
			}) .start();
		}
		Thread.sleep(10000);
		System.out.println(list.size());
	}
}

