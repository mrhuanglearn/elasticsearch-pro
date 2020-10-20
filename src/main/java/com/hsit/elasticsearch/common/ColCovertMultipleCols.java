package com.hsit.elasticsearch.common;

import java.util.*;

public class ColCovertMultipleCols {
    private volatile static HashMap hashMap;

    //人物名称
    private static final List<String> title = new ArrayList<>(
            Arrays.asList(
                    "title", "theme", "publisher", "otherChargePerson", "author", "collectorOrg", "collectorName", "agent", "ftNameOfFamily", "ftContact" //文献
                    , "surname", "zupu"//宗祠村落
                    , "title", "descr", "chineseName", "foreignName", "typeOf", "firstOf", "figure", "culture" //姓氏
            )
    );
    //时间
    private static final List<String> date = new ArrayList<>(
            Arrays.asList(
                    "publishDate"       //文献
                    , "timeOfCreation"   //宗祠村落
            )
    );
    //出洋与在外信息(对应摘要)
    private static final List<String> foreignInfo = new ArrayList<>(
            Arrays.asList(
                    "overView" //文献
                    , "division", "geo", "summary", "distribution" //宗祠村落
                    , "typeOf", "culture" //姓氏
            )
    );
    //地址
    private static final List<String> address = new ArrayList<>(
            Arrays.asList(
                    "ftGeoArea", "country"  //文献
                    , "chineseName", "foreignName", "geo", "distribution", "summary"    //宗祠村落
            )
    );
    //职位
    private static final List<String> position = new ArrayList<>(
            Arrays.asList(
                    "summary"   //宗祠村落
            )
    );
    //学阶
    private static final List<String> degree = new ArrayList<>(
            Arrays.asList(
                    "summary"   //宗祠村落
                    , "typeOf", "firstOf", "figure", "culture"   //姓氏
            )
    );
    //事件
    private static final List<String> incident = new ArrayList<>(
            Arrays.asList(
                    "summary"   //宗祠村落
                    , "typeOf", "firstOf", "figure", "culture"   //姓氏
            )
    );
    //成果
    private static final List<String> achievement = new ArrayList<>(
            Arrays.asList(
                    "typeOf", "firstOf", "culture"   //姓氏
            )
    );
    //荣誉
    private static final List<String> honor = new ArrayList<>(
            Arrays.asList(
                    "summary"   //宗祠村落
                    , "typeOf", "firstOf", "culture"   //姓氏
            )
    );
    //职业与擅长
    private static final List<String> profession = new ArrayList<>(
    );
    //纵向关系
    private static final List<String> vertical = new ArrayList<>(
    );
    //横向关心
    private static final List<String> horizontal = new ArrayList<>(
    );
    //亲戚
    private static final List<String> cognate = new ArrayList<>(
    );
    //谱牒
    private static final List<String> pd = new ArrayList<>(
            Arrays.asList(
                    "ftDonNum", "ftEditor", "title", "theme", "ftRevisionTime"  //文献
                    , "timeOfCreation", "zupu"  //宗祠村落
                    , "title", "descr"  //姓氏
            )
    );
    //堂号
    private static final List<String> donNum = new ArrayList<>(
            Arrays.asList(
                    "ftDonNum"  //文献
                    , "famousPerson", "summary" //宗祠村落
                    , "county", "hall", "firstOf", "figure", "culture" //姓氏
            )
    );

    //二次搜索字段名
    private static final List<String> secondSearch = new ArrayList<>(
            Arrays.asList(
                    "title", "theme", "publisher", "otherChargePerson", "author", "collectorOrg", "collectorName", "agent", "ftNameOfFamily", "ftContact"
            )

    );


    private ColCovertMultipleCols() {
    }

    public static HashMap<String, List<String>> getSingleton() {//懒加载单例模式
        if (hashMap == null) {
            synchronized (ColCovertMultipleCols.class) {
                if (hashMap == null) {
                    hashMap = new HashMap<String, List<String>>();
                    hashMap.put("title", title);
                    hashMap.put("date", date);
                    hashMap.put("foreignInfo", foreignInfo);
                    hashMap.put("address", address);
                    hashMap.put("position", position);
                    hashMap.put("degree", degree);
                    hashMap.put("incident", incident);
                    hashMap.put("achievement", achievement);
                    hashMap.put("honor", honor);
                    hashMap.put("profession", profession);
                    hashMap.put("vertical", vertical);
                    hashMap.put("horizontal", horizontal);
                    hashMap.put("cognate", cognate);
                    hashMap.put("pd", pd);
                    hashMap.put("donNum", donNum);
                    hashMap.put("secondSearch", secondSearch);
                }
            }
        }
        return hashMap;
    }

}
