package cn.mldn.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImproveLock implements Lock {
	private static Logger logger = LoggerFactory.getLogger(ImproveLock.class);
	private static final String ZOOKEEPER_IP_PORT = "47.100.49.95:2181,101.132.109.12:2181";
	private static final String LOCK_PATH = "/LOCK";
	private ZkClient client = new ZkClient(ZOOKEEPER_IP_PORT, 1000, 1000, new SerializableSerializer());
	private CountDownLatch latch;
	/**当前请求的节点前一个节点*/
	private String beforePath;
	/**当前节点*/
	private String currentPath;

	/**判断有没有 永久根节点 LOCK，没有则创建*/
	public ImproveLock() {
		if (!this.client.exists(LOCK_PATH)) {
			this.client.createPersistent(LOCK_PATH);
		}
	}

	public boolean tryLock() {
		// 如果currentPath为空则为第一次尝试加锁，第一次加锁赋值currentPath
		if (currentPath == null || currentPath.length() <= 0) {
			// 创建一个临时顺序节点
			currentPath = this.client.createEphemeralSequential(LOCK_PATH + '/', "lock");
			System.out.println("---------------------------->" + currentPath);
		}
		// 获取所有临时节点并排序，临时节点名称为自增长的字符串如：0000000400
		List<String> childrens = this.client.getChildren(LOCK_PATH);
		Collections.sort(childrens);
		if (currentPath.equals(LOCK_PATH + '/' + childrens.get(0))) {// 如果当前节点在所有节点中排名第一则获取锁成功
			return true;
		} else {// 如果当前节点在所有节点中排名中不是排名第一，则获取前面的节点名称，并赋值给beforePath
			int wz = Collections.binarySearch(childrens, currentPath.substring(6));
			beforePath = LOCK_PATH + '/' + childrens.get(wz - 1);
		}
		return false;
	}

	public void unlock() {
		// 删除当前临时节点
		client.delete(currentPath);
	}

	public void lock() {
		/**tryLock()尝试获取锁里面封装获取锁的逻辑；获取锁，返回true，没获取锁，返回false*/
		/**tryLock()返回true，直接让线程走出lock()方法即lock()方法执行完毕，继续执行lock()后面语句*/
		/**tryLock()返回false，则进入等待方法，遇到 latch.await();*/
		if (!tryLock()) {
			waitForLock();
			/**走出等待，再次去获取锁*/
			lock();
		} else {
			logger.info(Thread.currentThread().getName() + " 获得分布式锁！");
		}
	}

	private void waitForLock() {
		/**创建 IZkDataListener监听器对象，当监听节点状态发生变化时，调用监听器监听方法handleDataDeleted()*/
		IZkDataListener listener = new IZkDataListener() {
			public void handleDataDeleted(String dataPath) throws Exception {
				logger.info(Thread.currentThread().getName() + ":捕获到DataDelete事件！---------------------------");
				if (latch != null) {
					latch.countDown();
				}
			}

			public void handleDataChange(String dataPath, Object data) throws Exception {

			}
		};

		// 给排在前面的的节点增加数据删除的watcher
		/**subscribeDataChanges()给beforePath节点增加监听，开启多线程，死循环检查beforePath节点
		 * ，当该节点状态发生改变时，该线程调用监听器 listener.相应方法()*/
		this.client.subscribeDataChanges(beforePath, listener);
		if (this.client.exists(beforePath)) {
			latch = new CountDownLatch(1);
			try {
				/**没有获得锁的线程即当前节点，等待在这里，直到当前节点的前一个节点被删除，触发listener
				 * 对象的handleDataDeleted(String dataPath)方法时，latch.countDown()后，该
				 * 线程才会继续执行，走出lock()方法，即获得锁*/
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.client.unsubscribeDataChanges(beforePath, listener);
	}

	// ==========================================
	public void lockInterruptibly() throws InterruptedException {

	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	public Condition newCondition() {
		return null;
	}
}
