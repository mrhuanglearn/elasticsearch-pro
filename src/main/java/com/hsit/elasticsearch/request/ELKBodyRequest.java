package com.hsit.elasticsearch.request;


import cn.hutool.db.Page;

import java.util.List;

public class ELKBodyRequest extends Page {
    private String sDate;
    private String eDate;
    private String uDate;
    private String sessionId;
    private String[] xungenTypes;
    private List<InputCondition> inputConditions;

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String getuDate() {
        return uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String[] getXungenTypes() {
        return xungenTypes;
    }

    public void setXungenTypes(String[] xungenTypes) {
        this.xungenTypes = xungenTypes;
    }

    public List<InputCondition> getInputConditions() {
        return inputConditions;
    }

    public void setInputConditions(List<InputCondition> inputConditions) {
        this.inputConditions = inputConditions;
    }
}