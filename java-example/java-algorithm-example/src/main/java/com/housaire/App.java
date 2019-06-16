package com.housaire;

import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main1( String[] args ) throws InterruptedException
    {
        Thread t = new Thread(() -> {
            try
            {
                Thread.sleep(3000L);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        });
//        t.setDaemon(true);
        t.start();
        Thread.sleep(1000l);
        t.interrupt();
        Thread.sleep(1000L);
        System.out.println("结束");
    }



    public void sayHello()
    {
        System.out.println(this.getClass().getResource("").getPath());
    }

    public static void main(String[] args) throws CloneNotSupportedException
    {
        A a = new A(10);
        a.b = 5;
        a. c = -19;
        A b = a.clone();
        b.bo.ba = 109;
        System.out.println(b.a);
        System.out.println(b.b);
        System.out.println(b.c);
        System.out.println(b.bo.ba);
        System.out.println(a.bo.ba);

        System.out.println(new Date(1559616007000L).toLocaleString());
        System.out.println(new Date(1559616007000L).toLocaleString());
    }

    static class B implements Cloneable
    {

        private int ba;

        public B (int ba)
        {
            this.ba = ba;
        }

        @Override
        public B clone() throws CloneNotSupportedException
        {
            return (B) super.clone();
        }
    }

    static class A implements Cloneable
    {

        private transient final int a;

        private int b = 2;

        private static int c = 3;

        private B bo;

        public A(int a)
        {
            this.a = a;
            bo = new B(63);
        }

        public A clone() throws CloneNotSupportedException
        {
            A a = (A) super.clone();
            a.bo = a.bo.clone();
            return a;
        }

    }

}
