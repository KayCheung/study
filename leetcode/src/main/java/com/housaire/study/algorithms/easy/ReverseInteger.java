package com.housaire.study.algorithms.easy;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/1/28 16:27
 * @see
 * @since 1.0.0
 */
public class ReverseInteger
{
    public int reverse(int x) {
        long remind = 0;
        long reverse = 0;
        while(x != 0)
        {
            remind = x % 10;
            x = x / 10;
            reverse = reverse * 10 + remind;
        }
        if ((x >= 0 && reverse > Integer.MAX_VALUE) || (x < 0 && (reverse < Integer.MIN_VALUE || reverse > 0)))
        {
            return 0;
        }
        return (int) reverse;
    }

    public static void main(String[] args)
    {
        ReverseInteger ri = new ReverseInteger();
        System.out.println(ri.reverse(-2147483648));
        // 1534236469
        // 9876543210
    }

    /*class Solution {
        public int reverse(int x) {
            int result = 0;
            while(x != 0){
                if(result > Integer.MAX_VALUE / 10 || result < Integer.MIN_VALUE / 10)
                    return 0;
                result = result * 10 + x % 10;
                x /= 10;
            }
            return result;
        }
    }*/

}
