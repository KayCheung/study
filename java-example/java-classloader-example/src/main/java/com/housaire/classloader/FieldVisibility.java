package com.housaire.classloader;

/**
 * Created by kay on 2019/12/6.
 */
public class FieldVisibility {

    final Object obj;


    public FieldVisibility() {
        System.out.println("<init>()");
    }

    {
        new A(this);
        obj = new Object();
        System.out.println("normal block");
    }

    public static void main(String[] args) {
        new FieldVisibility();
    }

}

class A {

    public A(FieldVisibility fv) {
        fv.obj.toString();
    }

}
