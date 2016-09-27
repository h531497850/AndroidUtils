package android.hqs.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池工厂
 * 
 * @author hqs2063594
 */
public class ThreadFactory {

	/**
	 * 数据库、搜台等操作是单步的非并发执行。线程池（创建的这个线程池只有一个线程）
	 * ,该线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去！
	 */
	private static ExecutorService mSingleThread = Executors.newSingleThreadExecutor();
	/**
	 * 提交链式的，每次只执行一个任务
	 * @param task
	 * @return
	 */
	public static Future<?> linkTask(Runnable task) {
        Future<?> result = null;
        if (task != null && !mSingleThread.isTerminated() && !mSingleThread.isShutdown()) {
            result = mSingleThread.submit(task);
        }
        return result;
    }
	
	public static void sutLink(){
		mSingleThread.shutdown();
	}
	
	private static ExecutorService mAsynThread = Executors.newCachedThreadPool();
	/**
	 * 提交异步任务，不限定同时有几个任务执行
	 * @param task
	 * @return
	 */
	public static Future<?> asynTask(Runnable task) {
        Future<?> result = null;
        if (task != null && !mAsynThread.isTerminated() && !mAsynThread.isShutdown()) {
            result = mAsynThread.submit(task);
        }
        return result;
    }
	public static void shutAsyn(){
		mAsynThread.shutdown();
	}
	
}
