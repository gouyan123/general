package com.bjsxt.thread;
/**
 * 共享资源,并发(多个线程操作同一个对象，存在线程安全问题)
 * 
 * @author 裴新 QQ:3401997271
 *
 */
public class Web12306 implements Runnable{
	//票数
	private int ticketNums = 99;
	
	@Override
	public void run() {
		while(true) {
			if(ticketNums<0) {
				break;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+"-->"+ticketNums--);
		}
	}
	public static void main(String[] args) {
		//一份资源
		Web12306 web =new Web12306();
		System.out.println(Thread.currentThread().getName());
		//多个代理
		new Thread(web,"码畜").start();
		new Thread(web,"码农").start();
		new Thread(web,"码蟥").start();;
	}
}
