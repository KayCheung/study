package com.housaire.study.algorithms.hard;

import java.math.BigDecimal;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
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
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2)
    {
        // 需要做优化，可以只
        int[] largeArray = null;
        int[] smallArray = null;
        if (nums1.length > nums2.length)
        {
            largeArray = nums1;
            smallArray = nums2;
        }
        else
        {
            largeArray = nums2;
            smallArray = nums1;
        }
        int sumLength = nums1.length + nums2.length;
        int mergeArrayIndex = 0, largeArrayIndex = 0, smallArrayIndex = 0, middleLength = sumLength / 2 + 1;
        int[] mergeArray = new int[middleLength];
        int largeValue = 0, smallValue = 0;
        while (true)
        {
            if (largeArrayIndex < largeArray.length)
            {
                largeValue = largeArray[largeArrayIndex];
            }
            if (smallArrayIndex < smallArray.length)
            {
                smallValue = smallArray[smallArrayIndex];
            }
            if (largeArrayIndex < largeArray.length && smallArrayIndex < smallArray.length)
            {
                if (largeValue < smallValue)
                {
                    largeArrayIndex++;
                    mergeArray[mergeArrayIndex++] = largeValue;
                }
                else if (largeValue == smallValue)
                {
                    smallArrayIndex++;
                    mergeArray[mergeArrayIndex++] = smallValue;
                    if (mergeArrayIndex + 1 < middleLength)
                    {
                        largeArrayIndex++;
                        mergeArray[mergeArrayIndex++] = largeValue;
                    }
                }
                else
                {
                    smallArrayIndex++;
                    mergeArray[mergeArrayIndex++] = smallValue;
                }
            }
            else
            {
                if (largeArrayIndex < largeArray.length)
                {
                    largeArrayIndex++;
                    mergeArray[mergeArrayIndex++] = largeValue;
                }
                else
                {
                    smallArrayIndex++;
                    mergeArray[mergeArrayIndex++] = smallValue;
                }
            }
            if (mergeArrayIndex >= middleLength)
            {
                break;
            }
        }

        int finalValue = 0;
        if (sumLength % 2 == 0)
        {
            int index = sumLength / 2;
            finalValue = mergeArray[index - 1] + mergeArray[index];
        }
        else
        {
            int index = sumLength / 2;
            return mergeArray[index];
        }
        if (finalValue % 2 == 0)
        {
            return finalValue / 2;
        }
        else
        {
            return (finalValue / 2) + 0.5;
        }
    }

    public static void main(String[] args)
    {
        MedianOfTwoStoredArrays medianOfTwoStoredArrays = new MedianOfTwoStoredArrays();
        System.out.println(medianOfTwoStoredArrays.findMedianSortedArrays(new int[]{1,5,9}, new int[]{1,1,1}));
    }

}
