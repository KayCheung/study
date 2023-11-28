package com.housaire;

/**
 * Created by kay on 2019/9/16.
 */
public class ClassLoaderDemo {

    public static void main(String[] args) {
        System.out.println(A.class);
    }
}

class A {

    static {
        System.err.println("class A");
    }

}
