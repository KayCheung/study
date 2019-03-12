package com.housaire.study.algorithms;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/2/25 11:53
 * @see
 * @since 1.0.0
 */
public class IntegerLock {
    static Integer i=0;
    public static class AddThread extends Thread{
        public void run(){
            int k=0;
            for(;k<64;k++){
                synchronized(i){
                    i++;
                    /*if (k % 100 == 0)
                    {
                        System.out.println(Thread.currentThread().getName());
                    }*/
                }
            }
            System.out.println(Thread.currentThread().getName() + " = " + k);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        /*AddThread t1=new AddThread();
        AddThread t2=new AddThread();
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println(i);*/
        System.out.println((Integer)1111 == Integer.valueOf(1111));
    }
}
