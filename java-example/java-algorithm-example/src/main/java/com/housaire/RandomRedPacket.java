package com.housaire;

import java.util.Random;

public class RandomRedPacket
{

    public static double getRandomMoney(int remainSize, float remainMoney) {
        // remainSize 剩余的红包数量
        // remainMoney 剩余的钱
        if (remainSize == 1) {
            return (double) Math.round(remainMoney * 100) / 100;
        }
        Random r     = new Random();
        double min   = 0.01; //
        double max   = remainMoney / remainSize * 2;
        double money = r.nextDouble() * max;
        money = money <= min ? 0.01: money;
        money = Math.floor(money * 100) / 100;
        return money;
    }

    public static void main(String[] args)
    {
        float amount = 10;
        int num = 900;
        double total = 0;
        for (int i = num; i > 0; i--)
        {
            double money = RandomRedPacket.getRandomMoney(i, amount);
            total += money;
            amount = (float) (amount - money);
            System.out.println("第" + (num - i) + "个红包：" + money);
        }
        System.out.println(total);
    }

}
