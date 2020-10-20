package com.hsit.elasticsearch.abstracts;


import com.hsit.elasticsearch.interfaces.ElasticsearchClientInstance;

public abstract class ElasticsearchClientAbstract implements ElasticsearchClientInstance {


    protected String hostName = "127.0.0.1";
    protected int port = 9200;
    protected String scheme = "http";

    protected ElasticsearchClientAbstract() {
        super();
    }


    protected ElasticsearchClientAbstract(final String hostName, final int port, final String scheme) {
        super();
        this.hostName = hostName == null ? this.hostName : hostName;
        this.port = port == 0 ? this.port : port;
        this.scheme = scheme == null ? this.scheme : scheme;
    }


    public abstract void closeAndDestroy();


}
