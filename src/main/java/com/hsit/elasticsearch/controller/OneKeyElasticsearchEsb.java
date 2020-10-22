package com.hsit.elasticsearch.controller;


import com.hsit.elasticsearch.client.RestHighLevelClientInstance;
import com.hsit.elasticsearch.client.TransportClientInstance;
import com.hsit.elasticsearch.common.DisplayCols;
import com.hsit.elasticsearch.common.ELKTools;
import com.hsit.elasticsearch.entity.ResponseDataEntity;
import com.hsit.elasticsearch.operation.QueryOperation;
import com.hsit.elasticsearch.operation.QueryOperationCommon;
import com.hsit.elasticsearch.request.ELKBodyRequest;
import com.hsit.elasticsearch.request.ELKRequest;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

@Controller
public class OneKeyElasticsearchEsb {

    String ip;

    TransportClient client;

    RestHighLevelClient restHighLevelClient;

    @Value("${es.ip}")
    public void setPersonDao(String ip) throws UnknownHostException { // 用于属性的setter方法上
        Settings.Builder settings = Settings.builder().put("cluster.name", "one-key-search").put("client.transport.sniff", false);//集群名称
        client = TransportClientInstance.commonBuild(ip, 9300, settings.build());
        restHighLevelClient = RestHighLevelClientInstance.commonBuild(new HttpHost(ip, 9200, "http"));
        this.ip = ip;
    }


    public OneKeyElasticsearchEsb() throws UnknownHostException {
    }


    @RequestMapping("/set/session")
    @ResponseBody
    public void createAndSettingSession(@RequestBody ELKBodyRequest requests, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("requestCondition", requests);
    }


    @RequestMapping("/get/session")
    @ResponseBody
    public Object getSessionValue(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("requestCondition") == null) {
            return 0;
        }
        return session.getAttribute("requestCondition");
    }


    @RequestMapping("/index/search")
    @ResponseBody
    public Object indexSearch(@RequestBody ELKRequest elkRequest, HttpServletRequest request) {

        HttpSession session = request.getSession();
        QueryOperation operation = QueryOperation.builder();

        //搜索条件
        BoolQueryBuilder matchQuery = operation.oneKeySearchBool(elkRequest);
        if (session.getAttribute("requestCondition") == null) session.setAttribute("requestCondition", elkRequest);
        session.setAttribute("condition", matchQuery);

        String[] xungenTypes = elkRequest.getXungenTypes();
        if (ELKTools.isEmpty(xungenTypes)) {
            return new ResponseDataEntity();
        }
        String[] displayCols = null;
        List displayObj = DisplayCols.getSingleton().get(xungenTypes[0]);
        if (displayObj != null) {
            displayCols = (String[]) displayObj.toArray(new String[0]);
        }


        String[] docTypesFormat = xungenTypes[0].split("_");


        //设置高亮
        HighlightBuilder hiBuilder = operation.oneKeySearchBoolHighlight(displayCols);


        // 搜索请求实体类创建
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(docTypesFormat)
                .setQuery(matchQuery)
                .highlighter(hiBuilder);

        return QueryOperationCommon.builder().responseResult(client, searchRequestBuilder, elkRequest.getPageNumber(), elkRequest.getPageSize(), displayCols, true);
    }


    @RequestMapping("/first/search")
    @ResponseBody
    public Object firstSearch(@RequestBody ELKBodyRequest requests, HttpServletRequest request) {

        HttpSession session = request.getSession();
        QueryOperation operation = QueryOperation.builder();
        //搜索条件
        BoolQueryBuilder matchQuery = operation.oneKeyMultipleConditionBool(requests);
        if (session.getAttribute("requestCondition") == null) session.setAttribute("requestCondition", requests);
        session.setAttribute("condition", matchQuery);


        //设置高亮
        HighlightBuilder hiBuilder = operation.oneKeySearchBoolHighlightAll();


        String[] xungenTypes = requests.getXungenTypes();
        if (ELKTools.isEmpty(xungenTypes)) {
            return new ResponseDataEntity();
        }
        String[] displayCols = null;
        List displayObj = DisplayCols.getSingleton().get(xungenTypes[0]);
        if (displayObj != null) {
            displayCols = (String[]) displayObj.toArray(new String[0]);
        }

        String[] docTypesFormat = xungenTypes[0].split("_");

        // 搜索请求实体类创建
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(docTypesFormat)
                .setQuery(matchQuery)
                .highlighter(hiBuilder);


        return QueryOperationCommon.builder().responseResult(client, searchRequestBuilder, requests.getPageNumber(), requests.getPageSize(), displayCols, true);

    }


    @RequestMapping("/second/search")
    @ResponseBody
    public Object secondSearch(@RequestBody ELKRequest elkRequest, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //先前条件
        BoolQueryBuilder matchQuery = (BoolQueryBuilder) session.getAttribute("condition");
        //第二个条件
        QueryOperation operation = QueryOperation.builder();
        BoolQueryBuilder secondBoolQueryBuilder = operation.oneKeySecondSearchCondition(elkRequest);
        if (secondBoolQueryBuilder != null) boolQueryBuilder.must(secondBoolQueryBuilder);
        boolQueryBuilder.must(matchQuery);

        //设置高亮
        HighlightBuilder hiBuilder = operation.oneKeySearchBoolHighlightAll();

        String[] xungenTypes = elkRequest.getXungenTypes();
        if (ELKTools.isEmpty(xungenTypes)) {
            return new ResponseDataEntity();
        }
        String[] displayCols = null;
        List displayObj = DisplayCols.getSingleton().get(xungenTypes[0]);
        if (displayObj != null) {
            displayCols = (String[]) displayObj.toArray(new String[0]);
        }
        String[] docTypesFormat = xungenTypes[0].split("_");

        // 搜索请求实体类创建
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(docTypesFormat)
                .setQuery(boolQueryBuilder)
                .highlighter(hiBuilder);

        return QueryOperationCommon.builder().responseResult(client, searchRequestBuilder, elkRequest.getPageNumber(), elkRequest.getPageSize(), displayCols, true);


    }

    @RequestMapping("/second/count/search")
    @ResponseBody
    public Object secondSearchCount(@RequestBody ELKRequest elkRequest, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

//        先前条件
        BoolQueryBuilder matchQuery = (BoolQueryBuilder) session.getAttribute("condition");
//        第二个条件
        QueryOperation operation = QueryOperation.builder();
        BoolQueryBuilder secondBoolQueryBuilder = operation.oneKeySecondSearchCondition(elkRequest);
        if (secondBoolQueryBuilder != null) boolQueryBuilder.must(secondBoolQueryBuilder);
        boolQueryBuilder.must(matchQuery);

        String[] xungenTypes = elkRequest.getXungenTypes();
        if (ELKTools.isEmpty(xungenTypes)) {
            return new ResponseDataEntity();
        }

        return QueryOperationCommon.builder().responseCountResult(restHighLevelClient, boolQueryBuilder, xungenTypes);

    }


}
