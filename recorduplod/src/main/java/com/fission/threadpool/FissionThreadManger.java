package com.fission.threadpool;

import java.util.concurrent.ExecutorService;

/**
 * Created by lining on 2017/6/13.
 */

public class FissionThreadManger {
    private static FissionThreadManger threadManger;
    private static ExecutorService executorService;
    public static FissionThreadManger getInstance() {
        if (threadManger == null) {
            synchronized (FissionThreadManger.class) {
                if (threadManger == null){
                    threadManger = new FissionThreadManger();
                }
                initExecutorService();
            }
        }
        return threadManger;
    }
    private static void initExecutorService(){
        if(executorService == null){
            executorService = new PriorityExecutor();
        }
    }
    /**
     * 执行任务
     * @param priority 优先级 HIGH, NORMAL, LOW,如果null默认为NORMAL
     * @param runnable 需要执行的任务
     * */
    public void execute(Priority priority, Runnable runnable){
        if(priority == null){
            priority = Priority.NORMAL;
        }
        executorService.execute(new PriorityRunnable(priority, runnable));
    }

    /**终止当前任务*/
    public void shutDownNow(){
        executorService.shutdownNow();
    }

}
