package com.housaire;

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

    public static void main(String[] args)
    {
        /*char ret = 'A';
        int f = 0xFF;
        System.out.println(ret);
        System.out.println((int)ret);
        System.out.println(f);
        System.out.println(ret & f);*/

//        String maxCustomerNoStr = "TJ-00670";
//        Long maxCustomerNo = Long.parseLong(maxCustomerNoStr.substring(maxCustomerNoStr.indexOf("-") + 1));
//        System.out.println(maxCustomerNo);
//
//        System.out.println(String.format("%s-%05d", "SH", maxCustomerNo));
        System.out.println(-1 ^ (-1 << 12));
        System.out.println(Math.min(Runtime.getRuntime().availableProcessors() + 1, 32));

        System.out.println(Integer.parseInt("10.96"));

    }

}
