package com.housaire;

import com.sun.management.OperatingSystemMXBean;
import sun.misc.SharedSecrets;
import sun.misc.Unsafe;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DirectMemoryTest
{

    static final boolean is64bit = true;
    static final Unsafe unsafe = getUnsafe();

    static OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static void main(String[] args)
    {
//        ByteBuffer bb = ByteBuffer.allocateDirect(1024);
//        System.err.println(unsafe.getByte(printAddresses("BB: ", bb)));
//        System.out.println(bb);
        do
        {
            testDirectMemory();
        }
        while (SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed() > 0);
        printDirectMemory();
    }

    public static long printAddresses(String label, Object... objects) {
        long address = 0;
        System.out.print(label + ": 0x");
        long last = 0;
        int offset = unsafe.arrayBaseOffset(objects.getClass());
        int scale = unsafe.arrayIndexScale(objects.getClass());
        switch (scale) {
            case 4:
                long factor = is64bit ? 8 : 1;
                final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
                System.out.print(Long.toHexString(i1));
                address = i1;
                last = i1;
                for (int i = 1; i < objects.length; i++) {
                    final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
                    if (i2 > last)
                    {
                        System.out.print(", +" + Long.toHexString(i2 - last));
                    }
                    else
                    {
                        System.out.print(", -" + Long.toHexString(last - i2));
                    }
                    last = i2;
                }
                break;
            case 8:
                throw new AssertionError("Not supported");
        }
        System.err.println(address);
        return address;
    }

    static volatile ByteBuffer bb = null;

    static void testDirectMemory()
    {

        bb = ByteBuffer.allocateDirect(1024);
        printDirectMemory();
        System.err.println("\n====================================");
        printDirectMemory();
        System.err.println("\n====================================");
        List<byte[]> bts = new ArrayList<>();
        bb = null;
        for (int i = 1; i <= 10; i++)
        {
            bts.add(new byte[i * 1024]);
        }
//        System.gc();
//        int a = 1;
        Thread.yield();
        printDirectMemory();
    }

    static void printDirectMemory()
    {
        System.err.printf("\nTotalCapacity: %s\nReservedMemory: %s\n",
                SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity(),
                SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed());
    }

    static void printMemory()
    {
        // 总的物理内存
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize();
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        // 已使用的物理内存
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize());
        System.err.printf("总的物理内存: %s \n剩余的物理内存: %s \n已使用的物理内存: %s \n", totalMemorySize, freePhysicalMemorySize, usedMemory);
    }

    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

}
