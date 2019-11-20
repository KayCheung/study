package com.housaire;

import com.housaire.classloader.CustomClassLoader;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String name = "com.housaire.App";
        CustomClassLoader classLoader = new CustomClassLoader("E:\\SourceCode\\GitHub\\study\\java-example\\java-algorithm-example\\target\\classes\\");
        Class<App> clazz = (Class<App>) classLoader.loadClass(name);
        System.out.println(clazz.getMethods().length);
        App app = clazz.newInstance();
        app.sayHello();
    }

    public void sayHello()
    {
        System.out.println(this.getClass().getResource("").getPath());
    }
}
