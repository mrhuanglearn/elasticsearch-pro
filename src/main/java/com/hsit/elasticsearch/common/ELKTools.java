package com.hsit.elasticsearch.common;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ELKTools {
    /**
     * 判断是否为空
     *
     * @param objects
     * @return boolean
     */
    public static boolean isEmpty(Object... objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection
     * @return boolean
     */
    public static boolean isCollectionEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 判断集合是否为空
     *
     * @param s 字符串
     * @return boolean
     */
    public static boolean isStringNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * 字符串数组去重
     *
     * @param s 传入字符串
     * @return String[]
     */
    public static String[] deduplication(String... s) {
        if (isEmpty(s)) {
            return new String[]{};
        }
        List<String> result = new ArrayList<>();
        for (String str : s) {
            if (!result.contains(str))
                result.add(str);
        }
        return (String[]) result.toArray();
    }

}
