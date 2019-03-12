package com.housaire.study.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/2/25 14:22
 * @see
 * @since 1.0.0
 */
public class FlyweightLock
{
    static Integer i = 0;
    public static class AddThread extends Thread{
        public void run(){
            int k=0;
            for(;k<64;k++){
                synchronized(i){
                    i++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        int temp = 64 * 2;
        do
        {
            i = 0;
            AddThread t1 = new AddThread();
            AddThread t2 = new AddThread();
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println(i);
        } while(temp == i);
        System.out.println(i);
    }
}
