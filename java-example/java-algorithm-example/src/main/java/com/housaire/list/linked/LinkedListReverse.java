package com.housaire.list.linked;


/**
 * 链表逆序
 */
public class LinkedListReverse
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
        int size = 10;
        for (int i = 0; i < size; i++)
        {
            tail.data = i;
            if (i + 1 < size)
            {
                tail.next = new Node<>();
                tail = tail.next;
            }
        }

        tail = node;
        while (null != tail)
        {
            System.out.print(tail.data);
            tail = tail.next;
        }

        System.out.println();

        Node<Integer> newNode = null;
        Node<Integer> nextNode;
        while (null != node)
        {
            nextNode = node.next;
            node.next = newNode;
            newNode = node;
            node = nextNode;
        }

        tail = newNode;
        while (null != tail)
        {
            System.out.print(tail.data);
            tail = tail.next;
        }
    }

}
