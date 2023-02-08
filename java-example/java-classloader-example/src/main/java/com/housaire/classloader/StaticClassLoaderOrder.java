package com.housaire.classloader;

/**
 * Created by kay on 2019/12/5.
 */
public class StaticClassLoaderOrder {

    public static void main(String[] args) {
        System.out.println(ChildClass1.NAME);
    }

}

class SuperClass {

    static {
        System.out.println("SupperClass static block");

    }

    static int i = 10;

}

interface SuperInterface {

    String NAME = "SuperInterface";

}

interface ChildInterface extends SuperInterface {

    String CHILD_NAME = "ChildInterface";

}


class ChildClass1 extends SuperClass implements SuperInterface {

    static {
        System.out.println("ChildClass1 static block");
    }
    static int a = 20;

}
