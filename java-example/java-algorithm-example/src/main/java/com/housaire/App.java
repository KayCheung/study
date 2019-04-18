package com.housaire;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
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

}
