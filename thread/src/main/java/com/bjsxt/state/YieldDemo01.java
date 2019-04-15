package com.bjsxt.state;
/**
 * yield 礼让线程，暂停线程 直接进入就绪状态不是阻塞状态
 * 
 * @author 裴新 QQ:3401997271
 *
 */
public class YieldDemo01 {

	public static void main(String[] args) {
		MyYield my =new MyYield();
		new Thread(my,"a").start();
		new Thread(my,"b").start();
	}

}

class MyYield implements Runnable{

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+"-->start");
		Thread.yield(); //礼让
		System.out.println(Thread.currentThread().getName()+"-->end");
	}
	
}
