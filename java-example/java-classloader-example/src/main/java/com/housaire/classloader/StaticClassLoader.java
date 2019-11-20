package com.housaire.classloader;


import java.lang.reflect.Field;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/9/17 10:25
 * @see
 * @since 1.0.0
 */
public class StaticClassLoader
{

    public static void main(String[] args) throws Exception
    {
        System.out.println(Test.class);
//        System.out.println(Test.i);
        Field field = Test.class.getDeclaredFields()[0];
        field.setAccessible(true);
        try
        {
            System.out.println(field.get(Class.forName("com.housaire.classloader.Test")));
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
//        new Test();
        try
        {
            Class.forName("com.housaire.classloader.Test", false, Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }


    }

}

class Test {

    static int i = 0;

    final long l = 123;

    static {
        System.err.println("This is Test class static block!");
    }

    {
        System.err.println("This is Test class block");
    }

}