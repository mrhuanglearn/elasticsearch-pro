package com.hsit.elasticsearch.abstracts;

import com.hsit.elasticsearch.enums.AnalysisType;
import com.hsit.elasticsearch.interfaces.QueryOperationInterface;

import java.util.ArrayList;
import java.util.List;

public abstract class QueryOperationAbstract<T> implements QueryOperationInterface<T> {

    protected final List<AnalysisType> analysisTypes = new ArrayList<>();
    protected final List<String> fields = new ArrayList<>();


    /**
     * 添加要查询的field
     *
     * @param fields list数组参数
     * @return T
     */

    protected abstract T addField(List<String> fields);


    /**
     * 添加要查询的field
     *
     * @param field 动态field参数
     * @return T
     */
    protected abstract T addField(String... field);


    /**
     * 添加要查询分词类型 analysisType
     *
     * @param analysisTypes 分词类型(list 数组)
     * @return T
     */
    protected abstract T addAnalysisType(List<AnalysisType> analysisTypes);

    /**
     * 添加要查询分词类型 analysisTypes
     *
     * @param analysisTypes 分词类型动态参数
     * @return T
     */
    protected abstract T addAnalysisType(AnalysisType... analysisTypes);


}
