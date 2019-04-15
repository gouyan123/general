package com.bjsxt.state;

import java.lang.Thread.State;

/**
 * 观察线程的状态
 */
public class AllState {

	public static void main(String[] args) {
		Thread t = new Thread(()->{
			for(int i=0;i<5;i++) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(".....");
			}
		}) ;
		//观察状态
		State state = t.getState();
		System.out.println(state);  //NEW
		
		t.start(); 
		state = t.getState();   //RUNNABLE
		System.out.println(state);
		
		/*while(state != Thread.State.TERMINATED) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			state = t.getState();   //TIMED_WAITING
			System.out.println(state);
		}		
		state = t.getState();   //TERMINATED
		System.out.println(state);
		*/
		
		while(true) {
			//活动的线程数
			int num = Thread.activeCount();
			if(num==1) {
				break;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			state = t.getState();   //TIMED_WAITING
			System.out.println(state);
		}	
		

		
	}

}
