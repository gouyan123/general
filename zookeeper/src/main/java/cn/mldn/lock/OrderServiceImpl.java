package cn.mldn.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderServiceImpl implements Runnable {
	private static OrderCodeGenerator ong = new OrderCodeGenerator();

	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	// 同时并发的线程数
	private static final int NUM = 100;
	// 按照线程数初始化倒计数器,倒计数器
	private static CountDownLatch latch = new CountDownLatch(NUM);

	// private static Lock lock = new ReentrantLock();

	private Lock lock = new ImproveLock();

	/**创建订单*/
	public void createOrder() {
		String orderCode = null;
		/**执行lock.lock()，只有一个线程能走到继续执行的部分，继续执行lock.lock()后面的语句，即获得
		 * 锁；其他线程都走到等待的部分；*/
		lock.lock();
		try {
			/**执行到这里，说明该线程已获得锁，其他线程在等待*/
			// 获取订单编号
			orderCode = ong.getOrderCode();
			// ……业务代码，此处省略100行代码
			logger.info("insert into DB使用id：=======================>" + orderCode);
		} catch (Exception e) {
		} finally {
			/**释放锁*/
			lock.unlock();
		}
	}

	@Override
	public void run() {
		try {
			/**线程都等待在这里，直到 NUM个线程都创建完毕*/
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**多线程创建订单，争取锁*/
		createOrder();
	}

	public static void main(String[] args) {
		for (int i = 1; i <= NUM; i++) {
			// 按照线程数迭代实例化线程
			new Thread(new OrderServiceImpl()).start();
			// 创建一个线程，倒计数器减1
			latch.countDown();
		}
	}
}
