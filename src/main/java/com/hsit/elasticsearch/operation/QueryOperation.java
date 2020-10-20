package com.hsit.elasticsearch.operation;


import com.hsit.elasticsearch.common.ELKTools;
import com.hsit.elasticsearch.common.FieldHighlightBuilder;
import com.hsit.elasticsearch.enums.AnalysisType;
import com.hsit.elasticsearch.enums.Exact;
import com.hsit.elasticsearch.enums.LogicOperation;
import com.hsit.elasticsearch.common.ColCovertMultipleCols;
import com.hsit.elasticsearch.request.ELKBodyRequest;
import com.hsit.elasticsearch.request.ELKRequest;
import com.hsit.elasticsearch.request.InputCondition;
import com.sun.istack.internal.NotNull;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.regex.Pattern;

public class QueryOperation {

    private final static String FIELD_ANY = "ANY";//表示任意字段

    private final List<QueryBuilder> should = new ArrayList<>();
    private final List<QueryBuilder> must = new ArrayList<>();
    private final List<QueryBuilder> mustNot = new ArrayList<>();


    /**
     * 获取当前实例
     *
     * @return QueryOperation
     */
    public static QueryOperation builder() {
        return new QueryOperation();
    }


    /**
     * 获取 List<QueryBuilder> 的值
     *
     * @param text          关键字
     * @param analysisTypes 分词类型
     * @param fields        查询字段
     * @return QueryOperation
     */
    private List<QueryBuilder> operationCommon(String text, List<AnalysisType> analysisTypes, String... fields) {
        QueryOperationCommon operationCommon = QueryOperationCommon.builder().addField(fields);
        if (!ELKTools.isCollectionEmpty(analysisTypes)) {
            operationCommon.addAnalysisType(analysisTypes);
        }
        return operationCommon.builderPreConditionList(text);
    }

    /**
     * 获取 List<QueryBuilder> 的值
     *
     * @param text          关键字
     * @param analysisTypes 分词类型
     * @param fields        查询字段
     * @return QueryOperation
     */
    private List<QueryBuilder> operationCommon(String text, AnalysisType analysisTypes, String... fields) {
        QueryOperationCommon operationCommon = QueryOperationCommon.builder().addField(fields);
        operationCommon.addAnalysisType(Objects.requireNonNull(analysisTypes));
        return operationCommon.builderPreConditionList(text);
    }


    /**
     * 属性should添加 QueryBuilder
     *
     * @param text          关键字
     * @param analysisTypes 分词类型
     * @param fields        查询字段
     * @return QueryOperation
     */
    public QueryOperation should(String text, List<AnalysisType> analysisTypes, String... fields) {
        this.should.addAll(operationCommon(text, analysisTypes, fields));
        return this;
    }

    /**
     * 属性must添加 QueryBuilder
     *
     * @param text          关键字
     * @param analysisTypes 分词类型
     * @param fields        查询字段
     * @return QueryOperation
     */
    public QueryOperation must(String text, List<AnalysisType> analysisTypes, String... fields) {
        this.must.addAll(operationCommon(text, analysisTypes, fields));
        return this;
    }

    /**
     * 属性mustNot添加 QueryBuilder
     *
     * @param text          关键字
     * @param analysisTypes 分词类型
     * @param fields        查询字段
     * @return QueryOperation
     */
    public QueryOperation mustNot(String text, List<AnalysisType> analysisTypes, String... fields) {
        this.mustNot.addAll(operationCommon(text, analysisTypes, fields));
        return this;
    }

    /**
     * 属性should添加 QueryBuilder
     *
     * @param text          关键字
     * @param analysisTypes 分词类型
     * @param fields        查询字段
     * @return QueryOperation
     */
    public QueryOperation should(String text, AnalysisType analysisTypes, String... fields) {
        this.should.addAll(operationCommon(text, analysisTypes, fields));
        return this;
    }

    /**
     * 属性must添加 QueryBuilder
     *
     * @param text          关键字
     * @param analysisTypes 分词类型
     * @param fields        查询字段
     * @return QueryOperation
     */
    public QueryOperation must(String text, AnalysisType analysisTypes, String... fields) {
        this.must.addAll(operationCommon(text, analysisTypes, fields));
        return this;
    }

    /**
     * 属性mustNot添加 QueryBuilder
     *
     * @param text          关键字
     * @param analysisTypes 分词类型
     * @param fields        查询字段
     * @return QueryOperation
     */
    public QueryOperation mustNot(String text, AnalysisType analysisTypes, String... fields) {
        this.mustNot.addAll(operationCommon(text, analysisTypes, fields));
        return this;
    }


    /**
     * 属性should添加 QueryBuilder
     *
     * @param text   关键字
     * @param fields 查询字段
     * @return QueryOperation
     */
    public QueryOperation should(String text, String... fields) {
        this.should.addAll(operationCommon(text, (List<AnalysisType>) null, fields));
        return this;
    }

    /**
     * 属性must添加 QueryBuilder
     *
     * @param text   关键字
     * @param fields 查询字段
     * @return QueryOperation
     */
    public QueryOperation must(String text, String... fields) {
        this.must.addAll(operationCommon(text, (List<AnalysisType>) null, fields));
        return this;
    }

    /**
     * 属性mustNot添加 QueryBuilder
     *
     * @param text   关键字
     * @param fields 查询字段
     * @return QueryOperation
     */
    public QueryOperation mustNot(String text, String... fields) {
        this.mustNot.addAll(operationCommon(text, (List<AnalysisType>) null, fields));
        return this;
    }


    /**
     * 根据添加(should,must,mustNot)这些条件来实例BoolQueryBuilder对象
     *
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder build() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        this.should.forEach(boolQueryBuilder::should);
        this.must.forEach(boolQueryBuilder::must);
        this.mustNot.forEach(boolQueryBuilder::mustNot);
        return boolQueryBuilder;
    }


    /**
     * 判读是模糊查询还是精准查询
     *
     * @param isExact 传入参数:0,1
     * @return boolean
     */
    public boolean isAccurate(String isExact) {
        //精确查询字段
        return Exact.Y.getValue().equals(isExact);
    }

    /**
     * 判读是模糊查询还是精准查询
     *
     * @param exact 传入参数:Exact
     * @return boolean
     */
    public boolean isAccurate(Exact exact) {
        //精确查询字段
        return Exact.Y.equals(exact);
    }


    /****************************************上面为通用方法**********************************************/

    private final String[] fieldsAll = new String[]{
            "doc_type_name", "sub_type_name", "title", "doc_code", "classify_code"
            , "file_code", "ft_geo_area_name", "ft_name_of_family", "theme", "ft_revision_time", "collector_org", "ft_contact", "ft_contact_info"
            , "ft_editor", "ft_edit_count", "quantity", "units_name", "ft_disability_level_name", "version_type_wx", "version_type_pd", "carrier_name"
            , "price", "currency_name", "languages_name", "ft_don_num", "author", "translator", "publishing_house", "publish_date", "country_name", "location"
            , "negative_number", "collect_his", "pages", "solicitor", "solicitation_date", "agent", "entry_time", "sources_name", "certificate_code_name", "scan_number"
            , "medium_code_name", "open_level_code_name", "doc_make_name"
    };

    public BoolQueryBuilder oneKeyMultipleConditionBool(ELKBodyRequest elkBodyRequest) {
        QueryOperation queryOperation = QueryOperation.builder();
        String sDateField = "S_DATE", eDateField = "E_DATE", uDateField = "U_DATE";
        String sDateKeyWord = elkBodyRequest.geteDate(), eDateKeyWord = elkBodyRequest.getsDate(), uDateKeyWord = elkBodyRequest.getuDate();
        if (!Strings.isNullOrEmpty(sDateKeyWord))
            getAccurateOrFuzzyMust(queryOperation, Exact.Y, sDateKeyWord, sDateField);
        if (!Strings.isNullOrEmpty(eDateKeyWord))
            getAccurateOrFuzzyMust(queryOperation, Exact.Y, eDateKeyWord, eDateField);
        if (!Strings.isNullOrEmpty(uDateKeyWord))
            getAccurateOrFuzzyMust(queryOperation, Exact.Y, uDateKeyWord, uDateField);
        BoolQueryBuilder boolQueryBuilder = queryOperation.build();
        if (ELKTools.isCollectionEmpty(elkBodyRequest.getInputConditions())) {
            return boolQueryBuilder;
        }

        for (InputCondition item : elkBodyRequest.getInputConditions()) {
            BoolQueryBuilder boolQueryBuilderSub = oneKeySearchBool(item.getInputItemCondition(), false);
            if (boolQueryBuilderSub == null) continue;
            if ("1".equals(item.getGeneration())) {//此处待定,不知'与','或','非'类型.
                boolQueryBuilder.must(boolQueryBuilderSub);
            } else {
                boolQueryBuilder.should(boolQueryBuilderSub);
            }
        }
        return boolQueryBuilder;
    }

    /**
     * 获取封装好查询池对象
     *
     * @param requests 请求参数
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder oneKeySearchBool(ELKRequest requests) {
        return oneKeySearchBool(new ArrayList(Collections.singleton(requests)), true);
    }


    /**
     * 获取封装好查询池对象
     *
     * @param requests 请求参数
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder oneKeySearchBool(List<ELKRequest> requests) {
        return oneKeySearchBool(requests, true);
    }

    /**
     * 获取封装好查询池对象
     *
     * @param requests        请求参数
     * @param isNullSearchAll 当key为空时,是否查询全部
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder oneKeySearchBool(List<ELKRequest> requests, boolean isNullSearchAll) {
        return oneKeySearchBool(requests, isNullSearchAll, LogicOperation.OR);
    }

    /**
     * 获取封装好查询池对象
     *
     * @param requests        请求参数
     * @param isNullSearchAll 当key为空时,是否查询全部
     * @param logic           逻辑操作
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder oneKeySearchBool(List<ELKRequest> requests, boolean isNullSearchAll, LogicOperation logic) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        final int[] flag = {0};
        requests.forEach(request -> {
            String logicalOperation = request.getLogicalOperator();
            if (!Strings.isNullOrEmpty(request.getKeyword())) {
                if (Strings.isNullOrEmpty(logicalOperation)) {
                    logicalOperation = LogicOperation.OR.toString();
                }
                BoolQueryBuilder queryBuilder = oneKeySignalSearchBool(request, logic);
                setLogicBoolQuery(boolQueryBuilder, logicalOperation, queryBuilder);
                flag[0] = 1;
            }

        });
        if (flag[0] == 0) {//当keyword为空时查询全部
            if (isNullSearchAll) {
                boolQueryBuilder.should(QueryBuilders.matchAllQuery());
            } else {
                return null;
            }
        }
        return boolQueryBuilder;
    }


    /**
     * 获取当个查询条件对象
     *
     * @param requests 请求参数
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder oneKeySignalSearchBool(ELKRequest requests) {
        return oneKeySignalSearchBool(requests, LogicOperation.OR);
    }

    /**
     * 获取当个查询条件对象
     *
     * @param requests 请求参数
     * @param logic    逻辑操作
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder oneKeySignalSearchBool(ELKRequest requests, LogicOperation logic) {
        Pattern p = Pattern.compile("\\s+|\\+");//同义词用"+"号拼接
        String keyword = requests.getKeyword();
        String[] strings = p.split(keyword);
        QueryOperation queryOperation = QueryOperation.builder();

        if (FIELD_ANY.equals(requests.getFieldName())) {//任意字段查询
            getAccurateOrFuzzyShould(queryOperation, requests.getIsExact(), keyword, fieldsAll);
            return queryOperation.build();
        }
        HashMap<String, List<String>> hashMap = ColCovertMultipleCols.getSingleton();
        List<String> fields = hashMap.get(requests.getFieldName());

        for (String s : strings) {  //单个字段查询
            if (!ELKTools.isCollectionEmpty(fields)) {
                if (LogicOperation.AND.equals(logic)) {
                    getAccurateOrFuzzyMust(queryOperation, requests.getIsExact(), s, fields.toArray(new String[0]));
                } else {
                    getAccurateOrFuzzyShould(queryOperation, requests.getIsExact(), s, fields.toArray(new String[0]));
                }
            } else {
                if (LogicOperation.AND.equals(logic)) {
                    getAccurateOrFuzzyMust(queryOperation, requests.getIsExact(), s, requests.getFieldName());
                } else {
                    getAccurateOrFuzzyShould(queryOperation, requests.getIsExact(), s, requests.getFieldName());
                }
            }
        }
        return queryOperation.build();

    }


    /**
     * 获取当个查询条件对象
     *
     * @param requests 请求参数
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder oneKeySecondSearchCondition(ELKRequest requests) {
        String keyword = requests.getKeyword();
        if (ELKTools.isStringNullOrEmpty(keyword)) {
            return null;
        }
        return oneKeySignalSearchBool(requests);
    }


    /**
     * 设置boolQuery是否模糊查询条件
     *
     * @param queryOperation BoolQueryBuilder对象
     * @param isExact        是否模糊
     * @param keyword        关键字
     * @param fields         查询字段
     */
    public QueryOperation getAccurateOrFuzzyShould(QueryOperation queryOperation, String isExact, String keyword, String... fields) {
        if (isAccurate(isExact)) {//精确查询
            queryOperation.should(keyword, AnalysisType.ACCURATE, fields);
        } else {
            if (keyword.length() == 1) {//hanlp和ik 分词都不支持单字符查询,所以引入SIGNAL单字符分词查询
                queryOperation.should(keyword, AnalysisType.SIGNAL, fields);
            }
            queryOperation.should(keyword, AnalysisType.HANLP, fields);
        }
        return this;
    }

    /**
     * 设置boolQuery是否模糊查询条件
     *
     * @param queryOperation BoolQueryBuilder对象
     * @param isExact        是否模糊
     * @param keyword        关键字
     * @param fields         查询字段
     */
    public QueryOperation getAccurateOrFuzzyShould(QueryOperation queryOperation, Exact isExact, String keyword, String... fields) {
        return getAccurateOrFuzzyShould(queryOperation, isExact.getValue(), keyword, fields);
    }

    /**
     * 设置boolQuery是否模糊查询条件
     *
     * @param queryOperation BoolQueryBuilder对象
     * @param isExact        是否模糊
     * @param keyword        关键字
     * @param fields         查询字段
     */
    public QueryOperation getAccurateOrFuzzyMust(QueryOperation queryOperation, String isExact, String keyword, String... fields) {
        if (isAccurate(isExact)) {//精确查询
            queryOperation.must(keyword, AnalysisType.ACCURATE, fields);
        } else {
            if (keyword.length() == 1) {//hanlp和ik 分词都不支持单字符查询,所以引入SIGNAL单字符分词查询
                queryOperation.must(keyword, AnalysisType.SIGNAL, fields);
            }
            queryOperation.must(keyword, AnalysisType.HANLP, fields);
        }
        return this;
    }

    /**
     * 设置boolQuery是否模糊查询条件
     *
     * @param queryOperation BoolQueryBuilder对象
     * @param isExact        是否模糊
     * @param keyword        关键字
     * @param fields         查询字段
     */
    public QueryOperation getAccurateOrFuzzyMust(QueryOperation queryOperation, Exact isExact, String keyword, String... fields) {
        return getAccurateOrFuzzyMust(queryOperation, isExact.getValue(), keyword, fields);
    }


    /**
     * 设置逻辑查询池
     *
     * @param boolQueryBuilder 查询池
     * @param logicalOperation 逻辑标识符
     * @param queryBuilder     查询单个条件
     */
    private void setLogicBoolQuery(BoolQueryBuilder boolQueryBuilder, String logicalOperation, QueryBuilder queryBuilder) {
        if (LogicOperation.AND.toString().equals(logicalOperation)) {
            boolQueryBuilder.must(queryBuilder);
        } else if (LogicOperation.OR.toString().equals(logicalOperation)) {
            boolQueryBuilder.should(queryBuilder);
        } else if (LogicOperation.NOT.toString().equals(logicalOperation)) {
            boolQueryBuilder.mustNot(queryBuilder);
        }
    }


    /**
     * 指定日期字段将map里转换成yyyyMMdd
     *
     * @param getSourceMap
     * @param fields
     */
    private void convertDateFormat(Map getSourceMap, String[] fields) {
        for (String s : fields) {
            if (getSourceMap.containsKey(s))
                getSourceMap.put(s, getSourceMap.get(s).toString().substring(0, 10));

        }
    }


    /**
     * 添加高亮设置
     *
     * @return
     */
    public HighlightBuilder oneKeySearchBoolHighlight(@NotNull String... fields) {
        return FieldHighlightBuilder.builder().addAnalysisType(AnalysisType.HANLP, AnalysisType.ACCURATE, AnalysisType.SIGNAL).getHighlightBuilder(fieldsAll);

    }

    /**
     * 添加高亮设置
     *
     * @return
     */
    public HighlightBuilder oneKeySearchBoolHighlightAll() {
        HashMap<String, List<String>> hashMap = ColCovertMultipleCols.getSingleton();
        List<String> fields = new ArrayList<>();
        hashMap.forEach((k, val) -> {
            fields.addAll(val);
        });
        return FieldHighlightBuilder.builder().addAnalysisType(AnalysisType.HANLP, AnalysisType.ACCURATE, AnalysisType.SIGNAL).getHighlightBuilder(fields.toArray(new String[0]));

    }
}
