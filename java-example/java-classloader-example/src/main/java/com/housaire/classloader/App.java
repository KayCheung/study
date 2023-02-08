package com.housaire.classloader;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String name = "com.housaire.App";
        CustomClassLoader classLoader = new CustomClassLoader("/Users/kay/Workspace/SourceCode/study/java-example/java-algorithm-example/target/classes");
        Class<App> clazz = (Class<App>) classLoader.loadClass(name, true);
//        System.out.println(clazz.getMethods());
//        System.out.println(clazz.getMethods().length);
//        System.out.println(clazz.getDeclaredFields()[0].get(clazz));
//        App app = clazz.newInstance();
//        app.sayHello();
    }

    public void sayHello()
    {
        System.out.println(this.getClass().getResource("").getPath());
    }
}
