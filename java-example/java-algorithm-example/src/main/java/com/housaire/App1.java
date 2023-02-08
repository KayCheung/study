package com.housaire;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kay on 2019/12/7.
 */
public class App1 {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("runStateOf:  " + runStateOf(ctl.get()));
        System.out.println("SHUTDOWN:  " + SHUTDOWN);
        System.out.println("workerCountOf:  " + workerCountOf(ctl.get()));
        System.out.println("CAPACITY:  " + CAPACITY);
        System.out.println("isRunning:  " + isRunning(ctl.get()));

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("111");
            }
        });

        final SynchronousQueue queue = new SynchronousQueue();

        CountDownLatch cdl = new CountDownLatch(1);

        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cdl.countDown();
                    System.out.println("Queue->take: " + queue.take());
                    System.out.println("Queue->take: " + queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        cdl.await();
        System.out.println("Queue->offer:  " + queue.offer("abc"));
        queue.put("321");
        System.out.println("finished");

        cachedThreadPool.shutdown();
    }

}
