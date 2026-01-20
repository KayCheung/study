package com.housaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2025/4/7 0007
 * @Since 1.0
 */
public class App2 {

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        List<String> list2 = list1;
        list1.remove("b");
        System.out.println(list2);
        System.err.println(list1);
    }

}
