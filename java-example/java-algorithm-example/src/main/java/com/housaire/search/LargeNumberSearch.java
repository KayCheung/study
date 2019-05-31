package com.housaire.search;

import java.util.Arrays;

/**
 * 从40亿个正整数中查找一个正整数是否存在
 */
public class LargeNumberSearch
{

    // 用来存储数值
    private static byte[] numberStorage;
    // 每个字节最大存储8位数值
    private static int maxDigits = 8;
    // 字节最大数值
    private static int maxByte = 0x7f;
    // 字节数组起始索引偏移量
    private static int arrayStartOffset = 0;
    // 固定值
    private final static byte fixed = 1;
    // 0 是否存在
    private static boolean zeroHasExist = false;

    public static void load(int num)
    {
        load(0, num);
    }

    public static void load(int start, int end)
    {
        if (end <= 0 || end <= start || start < 0)
        {
            return;
        }

        long beginTime = System.currentTimeMillis();
        if (start == 0)
        {
            zeroHasExist = true;
            start = 1;
        }

        // 计算获得数组起始值的偏移量
        arrayStartOffset = getArrayStartOffset(start);

        // 获得最大数组长度
        int numberStorageSize = getNumberStorageSize(end);
        // 初始化数值存储对象
        numberStorage = new byte[numberStorageSize - arrayStartOffset + 1];

        for (int i = start; i <= end; i++)
        {
            calculateBit(i);
        }
        System.out.println("当前存储容量为：" + numberStorage.length + "  耗时：" + (System.currentTimeMillis() - beginTime) + " 毫秒");
    }

    public static void load(int[] nums)
    {
        if (null == nums || nums.length == 0)
        {
            return;
        }
        long beginTime = System.currentTimeMillis();
        // 需要对数组进行排序
        Arrays.sort(nums);

        // 计算获得数组起始值的偏移量
        arrayStartOffset = getArrayStartOffset(nums[0]);
        // 获得最大数组长度
        int numberStorageSize = getNumberStorageSize(nums[nums.length - 1]);
        // 初始化数值存储对象
        numberStorage = new byte[numberStorageSize - arrayStartOffset + 1];

        for (int num : nums)
        {
            calculateBit(num);
        }
        System.out.println("当前存储容量为：" + numberStorage.length + "  耗时：" + (System.currentTimeMillis() - beginTime) + " 毫秒");
    }

    private static void calculateBit(int i)
    {
        // 获得当前数值在字节数组中所在的下标
        int index = i / maxDigits - arrayStartOffset;
        // 如果当前数值可以整除maxDigits需要将下标减去1
        // 比如：8 / 8 = 1，但是一个字节可以存储8个数值，所以 8 获得的下标应该还是 0
        if (i % maxDigits == 0)
        {
            index--;
        }
        // 计算数组所在下标中, 表示的8个数值的最小值
        int min = i - (index * maxDigits);
        // 计算该数值在一个字节中所在的位
        // 比如 3 在字节中所在的位是：1 << 3 - 1, 0000 0100
        numberStorage[index] = (byte) (numberStorage[index] | ((fixed << (min - 1)) & maxByte));
    }

    private static int getArrayStartOffset(int start)
    {
        int arrayStartOffset = start / maxDigits;
        arrayStartOffset = arrayStartOffset - (start % maxDigits == 0 ? 1 : 0);
        return arrayStartOffset;
    }

    private static int getNumberStorageSize(int end)
    {
        return end / maxDigits + (end % maxDigits > 0 ? 1 : 0);
    }

    public static boolean search(int num)
    {
        if (num == 0 && zeroHasExist)
        {
            return true;
        }

        long beginTime = System.currentTimeMillis();
        // 获得当前数值在字节数组中所在的下标
        int index = num / maxDigits - arrayStartOffset;
        if (index >= numberStorage.length)
        {
            return false;
        }
        // 计算当前数值在8个数值中具体是哪个数值
        int digit = num - (index * maxDigits);
        // 查找该数值是否存在
        boolean exist = ((digit & numberStorage[index]) == digit);
        long endTime = System.currentTimeMillis() - beginTime;
        System.err.println("【 " + exist + " 】 - 数据 [ "
                + num + " ] 查找耗时：" + endTime
                + " 毫秒, 所在下标：" + index + "  下标值为：" + numberStorage[index]);

        return exist;
    }

    public static void main(String[] args)
    {
        int max = 8;
        LargeNumberSearch.load(max);
        for (int i = 0; i <= max + 1; i++)
        {
            boolean result = LargeNumberSearch.search(i);
            if (!result)
            {
                System.out.println("未查到指定的数值：" + i);
                break;
            }
        }

    }
}
