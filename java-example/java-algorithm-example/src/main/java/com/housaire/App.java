package com.housaire;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{

    public static int i = 8989123;

    static
    {
        System.err.println("this is java-algorithm-example App!");
    }

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

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.err.println(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.err.println(sdf.format(timestamp));

        System.out.println(a);

        System.out.println(a.getClass().getClassLoader());
        System.out.println(String.class.getClassLoader());

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
