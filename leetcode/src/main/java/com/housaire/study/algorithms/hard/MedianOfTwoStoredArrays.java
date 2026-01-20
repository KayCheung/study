package com.housaire.study.algorithms.hard;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description: 寻找两个有序数组的中位数
 * @date 2019/1/26 12:44
 * @see
 * @since 1.0.0
 */
public class MedianOfTwoStoredArrays
{

    /**
     * <pre>
     * There are two sorted arrays nums1 and nums2 of size m and n respectively.
     * Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).
     * You may assume nums1 and nums2 cannot be both empty.
     * Example 1:
     *   nums1 = [1, 3]
     *   nums2 = [2]
     *   The median is 2.0
     * Example 2:
     *   nums1 = [1, 2]
     *   nums2 = [3, 4]
     *   The median is (2 + 3)/2 = 2.5
     * </pre>
     * @param nums1 第一个有序数组
     * @param nums2 第二个有序数组
     * @return 中位数
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2)
    {
        // 确保 nums1 是较短的数组，以优化时间复杂度到 O(log(min(m,n)))
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        
        int m = nums1.length;
        int n = nums2.length;
        int left = 0, right = m;
        
        while (left <= right) {
            // 在 nums1 中的分割点
            int partitionX = (left + right) / 2;
            // 在 nums2 中的分割点，确保左半部分元素总数等于右半部分
            int partitionY = (m + n + 1) / 2 - partitionX;
            
            // 获取分割点左右的元素值
            int maxLeftX = (partitionX == 0) ? Integer.MIN_VALUE : nums1[partitionX - 1];
            int minRightX = (partitionX == m) ? Integer.MAX_VALUE : nums1[partitionX];
            
            int maxLeftY = (partitionY == 0) ? Integer.MIN_VALUE : nums2[partitionY - 1];
            int minRightY = (partitionY == n) ? Integer.MAX_VALUE : nums2[partitionY];
            
            // 检查是否找到了正确的分割点
            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                // 找到了正确的分割点
                if ((m + n) % 2 == 0) {
                    // 总长度为偶数，返回中间两个数的平均值
                    return (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
                } else {
                    // 总长度为奇数，返回左半部分的最大值
                    return Math.max(maxLeftX, maxLeftY);
                }
            } else if (maxLeftX > minRightY) {
                // nums1 的左半部分太大，需要向左移动
                right = partitionX - 1;
            } else {
                // nums1 的左半部分太小，需要向右移动
                left = partitionX + 1;
            }
        }
        
        // 理论上不会到达这里
        throw new IllegalArgumentException("输入的数组不是有序的");
    }

    public static void main(String[] args)
    {
        MedianOfTwoStoredArrays solution = new MedianOfTwoStoredArrays();
        
        // 测试用例1: [1,3], [2] -> 2.0
        System.out.println("测试1: " + solution.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
        
        // 测试用例2: [1,2], [3,4] -> 2.5
        System.out.println("测试2: " + solution.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}));
        
        // 测试用例3: [1,5,9], [1,1,1] -> 1.0
        System.out.println("测试3: " + solution.findMedianSortedArrays(new int[]{1, 5, 9}, new int[]{1, 1, 1}));
        
        // 测试用例4: [], [1] -> 1.0
        System.out.println("测试4: " + solution.findMedianSortedArrays(new int[]{}, new int[]{1}));
        
        // 测试用例5: [2], [] -> 2.0
        System.out.println("测试5: " + solution.findMedianSortedArrays(new int[]{2}, new int[]{}));
    }

}
