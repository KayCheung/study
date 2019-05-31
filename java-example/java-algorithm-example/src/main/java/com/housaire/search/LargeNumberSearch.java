package com.housaire.search;

import java.util.Arrays;

/**
 * 从40亿个正整数中查找一个正整数是否存在
 */
public class LargeNumberSearch
{

    // 用来存储数值
    private static byte[] numberStorage;
    // 一个字节最大能存储的位数（数值是否存在标志位）
    // （一个字节8bit，应该是可以存储8个数值的标志位，但是存在符号位，所以存储7位）后续需要解决
    private static int maxDigits = 7;
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
            storeNumToByte(i);
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
            storeNumToByte(num);
        }
        System.out.println("当前存储容量为：" + numberStorage.length + "  耗时：" + (System.currentTimeMillis() - beginTime) + " 毫秒");
    }

    private static void storeNumToByte(int i)
    {
        // 获得当前数值在字节数组中所在的下标
        int index = getArrayIndex(i);
        // 获得左位移数
        int offset = getDispacementOffset(i, index);
        // 计算该数值在一个字节中所在的位
        // 比如 3 在字节中所在的位是：1 << 3 - 1, 0000 0100
        numberStorage[index] = (byte) (numberStorage[index] | moveBitToOffset(offset));
    }

    private static byte moveBitToOffset(int offset)
    {
        if (offset == 7)
        {
            return (byte) (((fixed << offset) - 1) & maxByte);
        }
        return (byte) ((fixed << offset) & maxByte);
    }

    private static int getDispacementOffset(int num, int index)
    {
        // 计算当前数值在8个数值中具体是哪个数值
        return num - (index * maxDigits) - 1;
    }

    private static int getArrayIndex(int num)
    {
        // 获得当前数值在字节数组中所在的下标
        int index = num / maxDigits - arrayStartOffset;
        // 如果当前数值可以整除maxDigits需要将下标减去1
        // 比如：8 / 8 = 1，但是一个字节可以存储8个数值，所以 8 获得的下标应该还是 0
        if (num % maxDigits == 0)
        {
            index--;
        }
        return index;
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
        if (num == 0)
        {
            return zeroHasExist;
        }

        long beginTime = System.currentTimeMillis();
        // 获得当前数值在字节数组中所在的下标
        int index = getArrayIndex(num);
        if (index >= numberStorage.length)
        {
            return false;
        }
        // 获得左位移数
        int offset = getDispacementOffset(num, index);
        // 查找该数值是否存在
        byte actualByte = moveBitToOffset(offset);
        boolean exist = (actualByte & numberStorage[index]) == actualByte;
        long endTime = System.currentTimeMillis() - beginTime;
        System.err.println("【 " + (exist ? "找到" : "未找到") + " 】 - 数据 [ "
                + num + " ] 查找耗时：" + endTime
                + " 毫秒, 所在下标：" + index + "  下标值为：" + numberStorage[index] + " [" + Integer.toBinaryString(numberStorage[index]) + "]");

        return exist;
    }

    public static void main(String[] args)
    {
//        int max = 10008;
//        LargeNumberSearch.load(max);
        LargeNumberSearch.load(new int[] {1, 2, 5, 9, 10, 21, 99, 101, 112});
        for (int i = 0; i <= 112 + 1; i++)
        {
            boolean result = LargeNumberSearch.search(i);
            /*if (!result)
            {
                System.out.println("未查到指定的数值：" + i);
                break;
            }*/
        }
    }
}
