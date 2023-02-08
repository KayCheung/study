package com.housaire.list.linked;

/**
 * 链表三等份
 * 设三个链表：   第一个每循环一次走一步
 *              第二个每循环一次走两步
 *              第三个每循环一次走三步
 */
public class LinkedListDivideEqually
{

    static class Node<T>
    {

        T data;

        Node<T> next;

    }

    public static void main(String[] args)
    {
        Node<Integer> node = new Node<>();
        Node<Integer> tail = node;
        int size = 20000;
        for (int i = 0; i < size; i++)
        {
            tail.data = i;
            if (i + 1 < size)
            {
                tail.next = new Node<>();
                tail = tail.next;
            }
        }

        Node<Integer> first = node;
        Node<Integer> second = first.next;
        Node<Integer> three = second.next;

        while (null != three && null != three.next)
        {
            if (null != three.next.next)
            {
                three = three.next.next;
                second = second.next.next;
                first = first.next;
                if (null != three.next)
                {
                    three = three.next;
                }
            }
            else
            {
                three = three.next;
            }
        }
        System.out.println(first.data);
        System.out.println(second.data);
        System.out.println(three.data);
    }

}
