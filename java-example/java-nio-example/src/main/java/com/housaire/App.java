package com.housaire;

import com.housaire.reactor1.Reactor;
import com.housaire.reactor2.MainReactor;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException
    {
//        Thread reactor = new Thread(new Reactor(15326));
//        Thread reactor = new Thread(new MainReactor(15326));
//        reactor.start();
//        reactor.join();
        String s1 = new String("1");
        String s0 = s1.intern();
        String s2 = "1";
        System.out.println(s1 == s2);

        String s3 = new String("2") + new String("2");
        s3.intern();
        String s4 = "22";
        System.out.println(s3 == s4);

        B b = new B();
    }

}

class A {

    public A() {
        System.out.println("A()");
    }

    {
        System.out.println("A{}");
    }

    static {
        System.out.println("static A{}");
    }

}

class B extends A{

    public B() {
        System.out.println("B()");
    }

    {
        System.out.println("B{}");
    }

    static {
        System.out.println("static B{}");
    }

}
