package com.housaire;

/**
 * Created by kay on 2019/12/2.
 */
public class InnerClassDemo {

    public void sayHello() {
        System.out.println("Out say hello");
    }

    class StaticInnerClass {

        public void studing() {
            sayHello();
        }

    }

}
