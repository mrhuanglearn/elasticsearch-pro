package com.hsit.elasticsearch.operation;


import cn.hutool.core.map.MapUtil;
import com.hsit.elasticsearch.abstracts.QueryOperationAbstract;
import com.hsit.elasticsearch.common.ELKTools;
import com.hsit.elasticsearch.entity.ResponseDataEntity;
import com.hsit.elasticsearch.enums.AnalysisType;
import com.hsit.elasticsearch.enums.LogicOperation;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.*;

public class QueryOperationCommon extends QueryOperationAbstract<QueryOperationCommon> {


    private LogicOperation logicOperationType = LogicOperation.OR;

    /**
     * 获取当前实例
     *
     * @return QueryOperationCommon
     */
    public static QueryOperationCommon builder() {
        return new QueryOperationCommon();
    }


    /**
     * 添加要查询的field
     *
     * @param fields list数组参数
     * @return QueryOperationCommon
     */

    public QueryOperationCommon addField(List<String> fields) {
        if (ELKTools.isEmpty(fields)) {
            throw new IllegalArgumentException(" field must be not null");
        }
        fields.forEach(this::addField);
        return this;
    }


    /**
     * 添加要查询的field
     *
     * @param field 动态field参数
     * @return QueryOperationCommon
     */
    public QueryOperationCommon addField(String... field) {
        if (ELKTools.isEmpty(field)) {
            throw new IllegalArgumentException(" field must be not null");
        }
        this.fields.addAll(Arrays.asList(field));
        return this;
    }


    /**
     * 添加要查询分词类型 analysisType
     *
     * @param analysisTypes 分词类型(list 数组)
     * @return QueryOperationCommon
     */
    public QueryOperationCommon addAnalysisType(List<AnalysisType> analysisTypes) {
        if (!ELKTools.isEmpty(analysisTypes)) {
            analysisTypes.forEach(this::addAnalysisType);
        }
        return this;
    }

    /**
     * 添加要查询分词类型 analysisTypes
     *
     * @param analysisTypes 分词类型动态参数
     * @return QueryOperationCommon
     */
    public QueryOperationCommon addAnalysisType(AnalysisType... analysisTypes) {
        if (!ELKTools.isEmpty(analysisTypes)) {
            this.analysisTypes.addAll(Arrays.asList(analysisTypes));
        }
        return this;
    }


    /**
     * 根据先前赋值field和analysis基础条件,进行条件查询
     * <p>
     * fields属性必须不能为空数组
     *
     * @param text 查询关键词
     * @return BoolQueryBuilder
     */
    public List<QueryBuilder> builderPreConditionList(String text) {
        if (ELKTools.isCollectionEmpty(this.fields)) {
            throw new IllegalArgumentException(" fields must be not null");
        }
        List<QueryBuilder> queryBuilders = new ArrayList<>();
        this.fields.forEach(field -> {
            if (this.analysisTypes.size() == 0) {
                queryBuilders.add(signalQueryBuilder(text, field));
            } else {
                this.analysisTypes.forEach(analysisType -> {
                    queryBuilders.add(signalQueryBuilder(text, field, analysisType));
                });
            }

        });
        return queryBuilders;
    }


    /**
     * 根据先前赋值field和analysis基础条件,进行条件查询
     *
     * @param text  查询关键词
     * @param field 查询字段,不可为空
     * @return BoolQueryBuilder
     */
    public List<QueryBuilder> builderPreConditionList(String text, String... field) {
        String[] fields = Objects.requireNonNull(field, "field must not be null");
        return builderPreConditionList(text, Arrays.asList(fields));
    }

    /**
     * 根据先前赋值field和analysis基础条件,进行条件查询
     *
     * @param text  查询关键词
     * @param field 查询字段,不可为空(List数组)
     * @return BoolQueryBuilder
     */
    public List<QueryBuilder> builderPreConditionList(String text, List<String> field) {
        List<String> fields = Objects.requireNonNull(field, "field must not be null");
        this.addField(fields);
        return builderPreConditionList(text);
    }

    /**
     * 根据先前赋值field和analysis基础条件,进行条件查询
     *
     * @param text          查询关键词
     * @param analysisTypes 分词类型
     * @return BoolQueryBuilder
     */
    public List<QueryBuilder> builderPreConditionList(String text, AnalysisType... analysisTypes) {
        this.addAnalysisType(analysisTypes);
        return builderPreConditionList(text);
    }


    /**
     * 用单个字段和单个分词类型去查询
     *
     * @param field 查询字段名
     * @param text  查询关键字
     * @return
     */
    public QueryBuilder signalQueryBuilder(String text, String field) {
        return signalQueryBuilder(text, field, null);
    }


    /**
     * 用单个字段和单个分词类型去查询
     *
     * @param field         查询字段名
     * @param text          查询关键字
     * @param analysisTypes 分词类型
     * @return
     */
    public QueryBuilder signalQueryBuilder(String text, String field, AnalysisType analysisTypes) {
        StringBuffer stringBuffer = new StringBuffer(field);
        if (Objects.nonNull(analysisTypes)) {
            stringBuffer.append(".");
            stringBuffer.append(analysisTypes.getValue());
        }
        return QueryBuilders.matchQuery(stringBuffer.toString(), text);
    }


    /**
     * boolQueryBuilder 查询模板
     * <p>
     * json body example:
     * {
     * "query": {
     * "bool": {
     * "should": [{
     * "match_phrase": {
     * "title.hanlp": {
     * "query": "長氏",
     * "slop": 50
     * }                }            }]
     * }    }
     * }
     *
     * @param text  查询关键字
     * @param field 查询字段
     * @return BoolQueryBuilder
     */
    public List<QueryBuilder> listQueryBuilder(String text, String... field) {
        return listQueryBuilder(text, null, field);
    }


    public List<QueryBuilder> listQueryBuilder(String text, AnalysisType analysisType, String... field) {
        String[] fields = Objects.requireNonNull(field, "field must not be null");
        List<QueryBuilder> queryBuilders = new ArrayList<>();
        Arrays.asList(fields).forEach(value -> {
            queryBuilders.add(signalQueryBuilder(value, text, analysisType));
        });
        return queryBuilders;
    }


    public List<QueryBuilder> listQueryBuilder(String text, String field, AnalysisType... analysisTypes) {
        String finalField = Objects.requireNonNull(field, "field must not be null");
        List<QueryBuilder> queryBuilders = new ArrayList<>();
        if (ELKTools.isEmpty(analysisTypes)) {
            queryBuilders.add(signalQueryBuilder(finalField, text, null));
            return queryBuilders;
        }
        Arrays.asList(analysisTypes).forEach(analysisType -> {
            queryBuilders.add(signalQueryBuilder(finalField, text, analysisType));
        });
        return queryBuilders;
    }

    /**
     * 获取得数据进行处理
     *
     * @param hit 获取命中数据
     * @return Map
     */
    public Map dealWithResult(SearchHit hit) {
        return dealWithResult(hit, false);

    }

    /**
     * 获取得数据进行处理
     *
     * @param hit         获取命中数据
     * @param isHighlight 是否高亮
     * @return Map
     */
    public Map dealWithResult(SearchHit hit, boolean isHighlight) {
        return dealWithResult(hit, null, false);

    }


    /**
     * 获取得数据进行处理
     *
     * @param hit         获取命中数据
     * @param isHighlight 是否高亮
     * @return Map
     */
    public Map dealWithResult(SearchHit hit, String[] filterFields, boolean isHighlight) {
        Map getSourceMap = hit.getSourceAsMap();
        getSourceMap.remove("@timestamp");
        getSourceMap.remove("@version");
        getSourceMap.forEach((key, value) -> {
            if (value instanceof String) {
                value = ((String) value).replaceAll("<!\\[CDATA\\[", "");
                value = ((String) value).replaceAll("]]>", "");
                getSourceMap.put(key, value);
            }

        });
        if (!isHighlight) {
            return MapUtil.toCamelCaseMap(getSourceMap);
        }
        List filterFieldList = filterFields != null ? Arrays.asList(filterFields) : new ArrayList();
        hit.getHighlightFields().forEach((key, value) -> {//获取高亮
            Text[] text = hit.getHighlightFields().get(key).getFragments();
            StringBuffer textHightResult = new StringBuffer();
            for (Text str : text) {
                String getStr = (str.toString()).replaceAll("<!\\[CDATA\\[|\\[CDATA\\[", "");
                getStr = getStr.replaceAll("]]>", "");
                textHightResult.append(getStr);
            }
            String getKey = key;
            if (getKey.contains(".")) {
                getKey = key.substring(0, key.indexOf("."));
            }
            if (filterFieldList.size() == 0 || filterFieldList.contains(getKey)) {
                getSourceMap.put(getKey, textHightResult);
            }
        });
//        convertDateFormat(getSourceMap, dateFields);
        return MapUtil.toCamelCaseMap(getSourceMap);
    }

    /**
     * 返回(默认未高亮)处理数据
     *
     * @param client               transportClient 参数
     * @param searchRequestBuilder searchRequestBuilder cans
     * @param pageNum              页码
     * @param pageSize             一页数量
     * @return
     */
    public ResponseDataEntity responseResult(TransportClient client, SearchRequestBuilder searchRequestBuilder, int pageNum, int pageSize) {
        return responseResult(client, searchRequestBuilder, pageNum, pageSize, false);

    }

    /**
     * 返回(默认未高亮)处理数据
     *
     * @param client               transportClient 参数
     * @param searchRequestBuilder searchRequestBuilder cans
     * @param pageNum              页码
     * @param pageSize             一页数量
     * @param filterFields         指定返回字段
     * @return
     */
    public ResponseDataEntity responseResult(TransportClient client, SearchRequestBuilder searchRequestBuilder, int pageNum, int pageSize, String[] filterFields) {
        return responseResult(client, searchRequestBuilder, pageNum, pageSize, filterFields, false);
    }


    /**
     * 返回(默认未高亮)处理数据
     *
     * @param client               transportClient 参数
     * @param searchRequestBuilder searchRequestBuilder cans
     * @param pageNum              页码
     * @param pageSize             一页数量
     * @param isHighlight          是否高亮
     * @return
     */
    public ResponseDataEntity responseResult(TransportClient client, SearchRequestBuilder searchRequestBuilder, int pageNum, int pageSize, boolean isHighlight) {
        return responseResult(client, searchRequestBuilder, pageNum, pageSize, null, isHighlight);
    }


    /**
     * 返回(默认未高亮)处理数据
     *
     * @param client               transportClient 参数
     * @param searchRequestBuilder searchRequestBuilder cans
     * @param pageNum              页码
     * @param pageSize             一页数量
     * @param filterField          返回字段
     * @param isHighlight          是否高亮
     * @return
     */
    public ResponseDataEntity responseResult(TransportClient client, SearchRequestBuilder searchRequestBuilder, int pageNum, int pageSize, String[] filterField, boolean isHighlight) {


        //返回指定的字段
        if (!ELKTools.isEmpty(filterField)) {
            searchRequestBuilder.setFetchSource(filterField, null);
        }


        //获取Client对象,设置索引名称,搜索类型(SearchType.SCAN)[5.4移除，对于java代码，直接返回index顺序，不对结果排序],搜索数量,发送请求
        SearchResponse searchResponse = searchRequestBuilder.setSize(pageSize)
                .setSearchType(SearchType.DEFAULT)
                .setScroll(new TimeValue(1000))
                .execute().actionGet();
        //获取数据总和
        List resultList = new ArrayList();

        //获取总数量
        long totalCount = searchResponse.getHits().getTotalHits().value;
        double page = Math.ceil((float) totalCount / (pageSize));//计算总页数(暂时只计算单片)
        for (int i = 1; i <= page; i++) {
            if (pageNum == i) {
                SearchHit[] hits = searchResponse.getHits().getHits();
                for (SearchHit searchHit : hits) {
                    resultList.add(dealWithResult(searchHit, filterField, isHighlight));
                }
                //如果匹配到设置的分页，则此次循环退出
                break;
            }
            searchResponse = client
                    .prepareSearchScroll(searchResponse.getScrollId())//再次发送请求,并使用上次搜索结果的ScrollId
                    .setScroll(new TimeValue(1000)).execute()
                    .actionGet();
        }
        //清除滚屏
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(searchResponse.getScrollId());//也可以选择setScrollIds()将多个scrollId一起使用
        ActionFuture<ClearScrollResponse> clearScrollResponse = client.clearScroll(clearScrollRequest);
        boolean succeeded = clearScrollResponse.isDone();

        //数据封装
        ResponseDataEntity responseDataEntity = new ResponseDataEntity();
        responseDataEntity.setPageNum(pageNum);
        responseDataEntity.setPageSize(pageSize);
        responseDataEntity.setTotal(totalCount);
        responseDataEntity.setData(resultList);


        return responseDataEntity;
    }


}
