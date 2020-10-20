package com.hsit.elasticsearch.common;

import java.util.*;

public class DisplayCols {
    private volatile static HashMap hashMap;

    //文献
    private static final List<String> doc = new ArrayList<>(
            Arrays.asList("title", "author","overView","id")
    );
    //宗祠村落
    private static final List<String> clan_village = new ArrayList<>(
            Arrays.asList("chineseName", "geo","summary","id")
    );

    private DisplayCols() {
    }

    public static HashMap<String, List<String>> getSingleton() {//懒加载单例模式
        if (hashMap == null) {
            synchronized (DisplayCols.class) {
                if (hashMap == null) {
                    hashMap = new HashMap<String, List<String>>();
                    hashMap.put("doc", doc);
                    hashMap.put("clan_village", clan_village);
                }
            }
        }
        return hashMap;
    }

}
