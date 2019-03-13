package com.okayjam.bigdata.elasticsearch;

import com.okayjam.bigdata.util.PropertiesConfig;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/03/13 18:23
 **/
public class TransportClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(TransportClientFactory.class);

    private String HTTP_URL = PropertiesConfig.getValue("high.rest.http.url");// 多个ES搜索服务url以逗号分隔

    private TransportClient transportClient;

    private List<String> httpHostList = new ArrayList<>();

    private TransportClientFactory() {
        init();
    }

    public void init() {

        Settings settings = Settings.builder().put("client.transport.ignore_cluster_name", true).build();
        try {
            transportClient = new PreBuiltTransportClient(settings);

            String[] hostAndPortArray = HTTP_URL.split(",");
            for (int i = 0; i < hostAndPortArray.length; i++) {
                String host = hostAndPortArray[i].split(":")[0];
                String port = hostAndPortArray[i].split(":")[1];
                transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), Integer.parseInt(port)));
                httpHostList.add(hostAndPortArray[i]);
            }
            logger.info("Elasticsearch Java Transport Client init Successfully: {}", HTTP_URL);
        } catch (Exception e) {
            logger.error("Elasticsearch Java Transport Client init error: ", e);
        }
    }


    private static class SingletonInstance {

        private static final TransportClientFactory INSTANCE = new TransportClientFactory();
    }

    public static TransportClientFactory getInstance() {

        return SingletonInstance.INSTANCE;
    }
    public TransportClient getTransportClient() {

        if (transportClient == null) {
            logger.error("TransportClient init errror");
            return null;
        }
        return transportClient;
    }

    public void close() {
        if (transportClient != null) {
            try {
                transportClient.close();
            } catch (Exception e) {
                logger.error("close the TransportClient error:", e);
            }
        }
    }

}
