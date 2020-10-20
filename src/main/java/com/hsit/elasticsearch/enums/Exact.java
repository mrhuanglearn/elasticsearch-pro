package com.hsit.elasticsearch.enums;


/**
 * 精确查询或模糊查询
 * <p>
 * Y 精确, N 模糊
 */
public enum Exact {
    Y("1"), N("0");
    private final String value;

    Exact(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
