package com.hsit.elasticsearch.request;


import cn.hutool.db.Page;

public class ELKRequest extends Page {
    private String keyword;
    private String isExact;
    private String logicalOperator;
    private String fieldName;

    private String mediumCode;

    String[] xungenTypes;

    private String requests;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIsExact() {
        return isExact;
    }

    public void setIsExact(String isExact) {
        this.isExact = isExact;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMediumCode() {
        return mediumCode;
    }

    public void setMediumCode(String mediumCode) {
        this.mediumCode = mediumCode;
    }

    public String[] getXungenTypes() {
        return xungenTypes;
    }

    public void setXungenTypes(String[] xungenTypes) {
        this.xungenTypes = xungenTypes;
    }

    public String getRequests() {
        return requests;
    }

    public void setRequests(String requests) {
        this.requests = requests;
    }


}