package com.fission.threadpool;

/**
 * Created by iRuoBin on 2017/6/12.
 */

public class ThreadPoolTest {

    public static void lining() {
        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            if (i % 3 == 2) {
                FissionThreadManger.getInstance().execute(Priority.HIGH, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName()+"=执行优先级高="+finalI);
                        try{
                            Thread.sleep(200);
                        }catch(Exception e){

                        }
                    }
                });
            } else if (i % 3 == 1) {
                FissionThreadManger.getInstance().execute(Priority.LOW, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName()+"=执行优先级低="+finalI);
                        try{
                            Thread.sleep(200);
                        }catch(Exception e){

                        }
                    }
                });
            }else if (i % 3 == 0) {
                FissionThreadManger.getInstance().execute(Priority.NORMAL, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName()+"=执行优先级正常="+ finalI);
                        try{
                            Thread.sleep(200);
                        }catch(Exception e){

                        }
                    }
                });
            }
        }
    }

    public void iRuoBin() {

    }
}
