package com.housaire;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Desc:
 *
 * @author Zhang Kai
 * @email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date 2023/11/27
 * @since 1.0
 */
public class BufferTest {

    public static void compactTest() {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 0, 0, 0, 0, 0};
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        byte first = buffer.get();
        byte second = buffer.get();
        byte third = buffer.get();
        buffer.compact();
        System.out.println(Arrays.toString(bytes));
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        buffer.flip();
        first =  buffer.get();
        System.out.println(first);
    }

    public static void main(String[] args) {
        compactTest();
    }

}
