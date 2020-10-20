package com.hsit.elasticsearch.client;

import com.hsit.elasticsearch.abstracts.ElasticsearchClientAbstract;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class TransportClientInstance extends ElasticsearchClientAbstract {
    private volatile TransportClient transportClient;

    public TransportClientInstance() {
        this(null, 9300);
    }

    public TransportClientInstance(String hostName) {
        this(hostName, 9300);
    }

    public TransportClientInstance(int port) {
        this(null, port);
    }

    public TransportClientInstance(String hostName, int port) {
        super(hostName, port, null);
    }

    /**
     * 普通实例情况
     *
     * @param transportAddress
     * @return
     * @throws UnknownHostException
     */
    public static TransportClient commonBuild(TransportAddress... transportAddress) throws UnknownHostException {
        return new TransportClientInstance().getTransportClient(transportAddress);

    }

    public static TransportClient commonBuild(String hostName, TransportAddress... transportAddress) throws UnknownHostException {
        return new TransportClientInstance(hostName).getTransportClient(transportAddress);

    }

    public static TransportClient commonBuild(int port, TransportAddress... transportAddress) throws UnknownHostException {
        return new TransportClientInstance(port).getTransportClient(transportAddress);

    }

    public static TransportClient commonBuild(String hostName, int port, TransportAddress... transportAddress) throws UnknownHostException {
        return new TransportClientInstance(hostName, port).getTransportClient(transportAddress);

    }

    public static TransportClient commonBuild(Settings settings, TransportAddress... transportAddress) throws UnknownHostException {
        return new TransportClientInstance().getTransportClient(settings, transportAddress);

    }

    public static TransportClient commonBuild(String hostName, Settings settings, TransportAddress... transportAddress) throws UnknownHostException {
        return new TransportClientInstance(hostName).getTransportClient(settings, transportAddress);

    }

    public static TransportClient commonBuild(int port, Settings settings, TransportAddress... transportAddress) throws UnknownHostException {
        return new TransportClientInstance(port).getTransportClient(settings, transportAddress);

    }

    public static TransportClient commonBuild(String hostName, int port, Settings settings, TransportAddress... transportAddress) throws UnknownHostException {
        return new TransportClientInstance(hostName, port).getTransportClient(settings, transportAddress);

    }


    /**
     * 通过单例模式获取 TransportClient实例
     *
     * @param transportAddress
     * @return
     */
    public TransportClient getTransportClient(TransportAddress... transportAddress) throws UnknownHostException {
        return getTransportClient(null, transportAddress);
    }

    /**
     * 通过单例模式获取 TransportClient实例(适用 统一setting情况)
     *
     * @param transportAddress
     * @param settings
     * @return
     */
    public TransportClient getTransportClient(Settings settings, TransportAddress... transportAddress) throws UnknownHostException {
        if (Objects.isNull(settings))
            settings = Settings.EMPTY;
        if (transportClient == null) {
            synchronized (TransportClient.class) {
                if (transportClient == null) {
                    if (isEmpty(transportAddress)) {
                        transportClient = new PreBuiltTransportClient(settings)
                                .addTransportAddress(new TransportAddress(
                                        InetAddress.getByName(hostName), port));
                    } else {
                        transportClient = new PreBuiltTransportClient(settings)
                                .addTransportAddresses(transportAddress);
                    }
                }
            }
        }
        return transportClient;
    }


    /**
     * 正常情况下是不进行关闭和销毁,预防其他进程使用是client强制关闭连接
     */
    @Override
    public void closeAndDestroy() {
        if (transportClient != null)
            transportClient.close();
        transportClient = null;
    }
}
