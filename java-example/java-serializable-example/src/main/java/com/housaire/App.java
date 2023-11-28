package com.housaire;

import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println((long)Math.pow(2, 41));

        Date date = new Date(299, 11, 31, 23, 59, 59);
        System.out.println(date.toLocaleString());
        System.out.println(date.getTime());
        System.out.println(new Date().getTime());
    }
}
