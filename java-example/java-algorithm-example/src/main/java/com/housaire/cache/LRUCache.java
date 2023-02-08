package com.housaire.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kay on 2019/11/28.
 */
public class LRUCache extends LinkedHashMap {

    private int maxSize;

    public LRUCache(int maxSize)
    {
        super(0, 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > maxSize;
    }


    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(10);
        for (int i = 0; i < 10; i++)
        {
            lruCache.put(i, i);
        }
        System.out.println(lruCache);

        lruCache.get(0);
        lruCache.put(11, 11);
        System.out.println(lruCache);
    }


}
