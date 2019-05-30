package com.housaire;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/4/30 17:05
 * @see
 * @since 1.0.0
 */
public class AutoCloseDemo
{

    public static void main(String[] args)
    {
        try (AutoCloseableObject autoCloseableObject = new AutoCloseableObject())
        {
            autoCloseableObject.sayHello();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("我执行完啦。。。！！！");
        }
    }

}

class AutoCloseableObject implements AutoCloseable {

    public void sayHello()
    {
        System.out.println("大家好。。。！！！");
        throw new RuntimeException("我抛异常啦。。。！！！");
    }

    @Override
    public void close() throws Exception
    {
        System.err.println("我关闭啦。。。。。");
    }
}
