package com.housaire;

/**
 * Created by kay on 2019/12/24.
 */
public class ThreadDemo {

    public static void main(String[] args) {
        System.out.println("starting main: " + Thread.currentThread().getName());
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "running start");
                        Thread.sleep(500L);
                        System.out.println(Thread.currentThread().getName() + "running end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread.currentThread().yield();
        System.out.println("stoped main: " + Thread.currentThread().getName());
    }

}
