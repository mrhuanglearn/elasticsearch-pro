package com.hsit.elasticsearch.entity;

import java.util.List;
import java.util.Map;

public class ResponseDataEntity {
    int pageNum = 0;
    int pageSize = 0;
    long total = 0;
    List<Map> data;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }
}
