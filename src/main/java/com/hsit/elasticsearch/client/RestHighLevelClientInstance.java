package com.hsit.elasticsearch.client;


import com.hsit.elasticsearch.abstracts.ElasticsearchClientAbstract;
import com.hsit.elasticsearch.aops.CGlibProxyFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.net.UnknownHostException;

public class RestHighLevelClientInstance extends ElasticsearchClientAbstract {


    private volatile RestHighLevelClient restHighLevelClient;

    private volatile RestClientBuilder restClientBuilder;


    /**
     * 获取实例Elasticsearch客户端实例
     */
    public RestHighLevelClientInstance() {
    }


    public RestHighLevelClientInstance(final String hostName, final int port, final String scheme) {
        super(hostName, port, scheme);
    }

    public RestHighLevelClientInstance(final String hostName, final int port) {
        super(hostName, port, null);
    }

    public RestHighLevelClientInstance(final String hostName) {
        super(hostName, 0, null);
    }


    /**
     * 普通实例情况
     *
     * @param hosts
     * @return
     * @throws UnknownHostException
     */
    public static RestHighLevelClient commonBuild(HttpHost... hosts) {
        return new RestHighLevelClientInstance().getRestHighLevelClient(hosts);
    }


    public static RestHighLevelClient commonBuild(MaxConnConfig maxConnConfig, HttpHost... hosts) throws UnknownHostException {
        return commonBuild(null, maxConnConfig, hosts);

    }

    public static RestHighLevelClient commonBuild(ConnectConfig connectConfig, HttpHost... hosts) throws UnknownHostException {
        return commonBuild(connectConfig, null, hosts);

    }


    public static RestHighLevelClient commonBuild(ConnectConfig connectConfig, MaxConnConfig maxConnConfig, HttpHost... hosts) throws UnknownHostException {
        RestHighLevelClientInstance cGlibProxyFactory = CGlibProxyFactory.getInstance().getProxy(RestHighLevelClientInstance.class);
        RestClientBuilder restBuilder = cGlibProxyFactory.getSingleRestClientBuilder(hosts);
        if (connectConfig != null) {
            cGlibProxyFactory.setConnectTimeOutConfig(connectConfig);
        }

        if (maxConnConfig != null) {
            cGlibProxyFactory.setMultipleConnectConfig(maxConnConfig);
        }
        return new RestHighLevelClient(restBuilder);
    }


    /**
     * 获取 RestHighLevelClient 单例实例
     *
     * @param hosts
     * @return
     */
    public RestHighLevelClient getRestHighLevelClient(HttpHost... hosts) {
        RestHighLevelClientInstance cGlibProxyFactory = CGlibProxyFactory.getInstance().getProxy(RestHighLevelClientInstance.class);
        if (restHighLevelClient == null) {
            RestClientBuilder restClientBuilder1=  cGlibProxyFactory.getSingleRestClientBuilder(hosts);
            cGlibProxyFactory.setConnectTimeOutConfig(new ConnectConfig());
            cGlibProxyFactory.setMultipleConnectConfig(new MaxConnConfig());
            synchronized (RestHighLevelClient.class) {
                if (restHighLevelClient == null) {
                    restHighLevelClient = new RestHighLevelClient(restClientBuilder1);
                }
            }
        }
        return restHighLevelClient;
    }

    /**
     * 单例模式:实例RestClientBuilder(双重加锁)
     *
     * @param hosts
     * @return
     */
    public RestClientBuilder getSingleRestClientBuilder(HttpHost... hosts) {
        if (restClientBuilder == null) {
            synchronized (RestClientBuilder.class) {
                if (restClientBuilder == null) {
                    if (isEmpty(hosts)) {
                        restClientBuilder = RestClient.builder(
                                new HttpHost(hostName, port, scheme));
                    } else {
                        restClientBuilder = RestClient.builder(hosts);
                    }
                }
            }
        }
        return restClientBuilder;
    }

    public class ConnectConfig {
        int connectTimout = 30000;
        int socketTimeout = 30000;
        int connectionRequestTimeout = 30000;

        public ConnectConfig() {
        }

        public ConnectConfig(int connectTimout, int socketTimeout, int connectionRequestTimeout) {
            this.connectTimout = connectTimout;
            this.socketTimeout = socketTimeout;
            this.connectionRequestTimeout = connectionRequestTimeout;
        }

    }

    public class MaxConnConfig {
        int maxConnTotal = 16;
        int maxConnPerRoute = 16;

        public MaxConnConfig() {
        }

        public MaxConnConfig(int maxConnTotal, int maxConnPerRoute) {
            this.maxConnTotal = maxConnTotal;
            this.maxConnPerRoute = maxConnPerRoute;
        }
    }

    /**
     * 主要关于异步httpclient的连接延时配置
     *
     * @param config
     */
    public void setConnectTimeOutConfig(ConnectConfig config) {
        this.restClientBuilder.setRequestConfigCallback(requestConfigBuilder1 -> {
            requestConfigBuilder1.setConnectTimeout(config.connectTimout);
            requestConfigBuilder1.setSocketTimeout(config.socketTimeout);
            requestConfigBuilder1.setConnectionRequestTimeout(config.connectionRequestTimeout);
            return requestConfigBuilder1;
        });
    }

    /**
     * 主要关于异步httpclient的连接数配置
     *
     * @param maxConnConfig
     */
    public void setMultipleConnectConfig(MaxConnConfig maxConnConfig) {
        this.restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnConfig.maxConnTotal);
            httpClientBuilder.setMaxConnPerRoute(maxConnConfig.maxConnPerRoute);
            return httpClientBuilder;
        });
    }


    /**
     * 正常情况下是不进行关闭和销毁,预防其他进程使用是client强制关闭连接
     */
    @Override
    public void closeAndDestroy() {
        try {
            if (restHighLevelClient != null)
                restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            restHighLevelClient = null;
        }
    }
}
