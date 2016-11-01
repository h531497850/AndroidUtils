package android.hqs.helper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
	
	/**
	 * 核心线程数，核心线程会一直存活，即使没有任务需要处理。当线程数小于核心线程数时，即使现有的线程空闲，
	 * 线程池也会优先创建新线程来处理任务，而不是直接交给现有的线程处理。
	 * 核心线程在allowCoreThreadTimeout被设置为true时会超时退出，默认情况下不会退出。
	 */
	private static final int CORE_POOL_SIZE = 5;
	/**
	 * 当线程数大于或等于核心线程，且任务队列已满时，线程池会创建新的线程，直到线程数量达到maximumPoolSize。
	 * 如果线程数已等于maximumPoolSize，且任务队列已满，则已超出线程池的处理能力，线程池会拒绝处理任务而抛出异常。
	 */
	private static final int MAXIMUM_POOL_SIZE = 128;
	/**
	 * 当线程空闲时间达到keepAliveTime，该线程会退出，直到线程数量等于corePoolSize。
	 * 如果allowCoreThreadTimeout(是否允许核心线程空闲退出，默认值为false)设置为true，则所有线程均会退出直到线程数量为0。
	 */
	private static final int KEEP_ALIVE_TIME = 1;
	/**
	 * 表示存放任务的队列（存放需要被线程池执行的线程队列）。 
	 */
	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);

	/**
	 * 最多只有CORE_POOL_SIZE个线程同时运行，多于的等待或丢弃
	 * 当线程数小于核心线程数时，创建线程。
	 * 当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。
	 * 当线程数大于等于核心线程数，且任务队列已满:
	 * 		若线程数小于最大线程数，创建线程
	 * 		若线程数等于最大线程数，抛出异常，拒绝任务
	 * 
	 * TimeUnit.SECONDS KEEP_ALIVE_TIME的单位为秒
	 * 
	 * handler拒绝策略（添加任务失败后如何处理该任务）. 
	 * 
	 * TODO 如果某任务空闲（休眠、等待）达到一秒钟，该任务会被放弃。
	 * 
	 * TODO 假如有500个任务
	 *      1、执行1~5
	 *      2、6~15放入BlockingQueue栈
	 *      3、执行16~128
	 *      4、129~500被放弃
	 *      5、最终执行顺序为1~5/16~128/6~15
	 */
	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
			CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
			sPoolWorkQueue, new ThreadPoolExecutor.DiscardOldestPolicy());
	
}
