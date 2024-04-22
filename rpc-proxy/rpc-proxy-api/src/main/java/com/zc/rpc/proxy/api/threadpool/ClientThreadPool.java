package com.zc.rpc.proxy.api.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 执行回调任务的线程池
 * @Author Schrodinger's Cobra
 * @Date 2024-02-05
 */
public class ClientThreadPool {
    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536),
                r -> new Thread(r, "CallBack Thread"));

    }

    public static void submit(Runnable task){
        threadPoolExecutor.submit(task);
    }

    public static void shutdown(){
        threadPoolExecutor.shutdown();
    }

}
