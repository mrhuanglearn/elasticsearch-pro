package com.hsit.elasticsearch.enums;

/**
 * template模板定义得分词类型
 * <p>
 * FPY 全拼音 ,SPY 简拼音
 * ,SIGNAL 单个字符 ,ACCURATE 精确 ,HANLP 细粒分词
 */
public enum AnalysisType {
    FPY("fpy"), SPY("spy"), SIGNAL("signal"), ACCURATE("accurate"), HANLP("hanlp");

    private final String value;

    AnalysisType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
