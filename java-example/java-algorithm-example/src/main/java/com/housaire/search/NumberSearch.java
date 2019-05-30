package com.housaire.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 从40亿个整数中查找一个数是否存在
 */
public class NumberSearch
{

    static List<Integer> getRandomNum()
    {
        List<Integer> nums = new ArrayList<>();
        for (int i = 0; i < 20; i++)
        {
            nums.add((int) (Math.random() * Integer.MAX_VALUE));
        }
        return nums;
    }

    public static void main(String[] args)
    {
        byte[] bytes = new byte[Integer.MAX_VALUE / 8];

        List<Integer> nums = getRandomNum();

        long beginTime = System.currentTimeMillis();

        int index;
        for (int i = 0; i < Integer.MAX_VALUE - 8; i++)
        {
            if (nums.contains(i))
            {
                nums = getRandomNum();
                continue;
            }
            index = i / 8;
            byte b = bytes[index];
            bytes[index] = (byte) (b | (i - (index * 8)));
        }

        System.err.println("数据加载耗时：" + (System.currentTimeMillis() - beginTime) + " 毫秒");


        for (int i = 0; i < 20; i++)
        {
            int searchNum = (int) (Math.random() * Integer.MAX_VALUE);
            beginTime = System.currentTimeMillis();

            index = searchNum / 8; // 获得byte下标
            int n = searchNum - (index * 8);
            System.out.println((n & bytes[index]) == n);
            System.err.println("数据 [ " + searchNum + " ] 查找耗时：" + (System.currentTimeMillis() - beginTime) + " 毫秒, 所在下标：" + index + "  下标值为：" + bytes[index]);
        }
        // 下标 * 8 等于 当前下标 8个数的起始值
        // 当前查找的数值减去起始值则得到一个8以内的数

        byte b = 5;
        int i = 5;
        System.out.println((b & i) == i);
    }

}
