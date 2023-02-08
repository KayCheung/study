package com.housaire;

/**
 * Created by kay on 2019/12/12.
 */
public class IntReverse {

    public static int reverse(int num) {
        int rev = 0;
        while (num != 0) {
            rev = rev * 10 + num % 10;
            num /= 10;
        }
        return rev;
    }

    public static void main(String[] args) {
        System.out.println(reverse(-199));
    }

}
